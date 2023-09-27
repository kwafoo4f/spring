package cpm.kuafoo4j.spring;

import cpm.kuafoo4j.spring.annatation.*;

import java.beans.Introspector;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: zk
 * @date: 2023-09-27 15:20
 */
public class SpringApplicationContext {

    private static final String SINGLETON = "singleton";
    private static final String PROTOTYPE = "prototype";

    private Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();
    // 单例池
    private Map<String,Object> singletonBeanMap = new HashMap<>();


    public SpringApplicationContext(Class<?> clazz) {
        // 1.扫描bean,封装为beanDefinition
        scan(clazz);

        // 2.创建单例非懒加载的bean
        createSingletonBean();

        // 3.打印banner
        printBanner();
    }

    /**
     * 打印banner
     */
    private void printBanner() {
        System.out.println("                                //  ) )              \n" +
                "   / ___               ___   __//__  ___      ___    \n" +
                "  //\\ \\     //   / / //   ) ) //   //   ) ) //   ) ) \n" +
                " //  \\ \\   //   / / //   / / //   //   / / //   / /  \n" +
                "//    \\ \\ ((___( ( ((___( ( //   ((___/ / ((___/ /   \n");
        System.out.println("SpringApplicationContext init success!");
    }

    private void createSingletonBean() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
            // 创建单例非懒加载的bean
            if (SINGLETON.equals(beanDefinition.getScope())
                    && !beanDefinition.isLazy()) {
                Object singletonBean = createBean(beanName);
                singletonBeanMap.put(beanName,singletonBean);
            }
        }
    }

    /**
     * 扫描bean,封装为beanDefinition
     * @param clazz
     */
    private void scan(Class<?> clazz) {
        // 获取扫描路径
        ComponentScan ComponentScanAnnotation = clazz.getAnnotation(ComponentScan.class);
        String packagePath = ComponentScanAnnotation.value();
        ClassLoader classLoader = clazz.getClassLoader();
        String sysPath = packagePath.replace(".", "//");
        URL url = classLoader.getResource(sysPath);
        // 扫描文件
        File file = new File(url.getFile());
        scanFile(file,classLoader);
    }

    /**
     * 扫描文件,使用类加载器将文件加载为class
     * @param file
     * @param classLoader
     */
    private void scanFile(File file,ClassLoader classLoader) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    // 是文件夹继续进入文件夹扫描
                    scanFile(f,classLoader);
                    continue;
                }

                // 使用类加载器加载文件
                String absolutePath = f.getAbsolutePath();
                String sysClassPath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                String classPath = sysClassPath.replace("\\", ".");
                try {
                    Class<?> clazz = classLoader.loadClass(classPath);
                    if (clazz.isAnnotationPresent(Component.class)) {
                        // 是需要处理的bean
                        buildAndAddBeanDefinition(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 构建BeanDefinition并加入到缓存中
     * @param clazz
     */
    private void buildAndAddBeanDefinition(Class<?> clazz) {
        Component componentAnnotation = clazz.getAnnotation(Component.class);
        String beanName = !"".equals(componentAnnotation.value()) ? componentAnnotation.value()
                : Introspector.decapitalize(clazz.getSimpleName());;
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setType(clazz);
        beanDefinition.setLazy(clazz.isAnnotationPresent(Lazy.class));
        if (clazz.isAnnotationPresent(Scope.class)) {
            beanDefinition.setScope(clazz.getAnnotation(Scope.class).value());
        } else {
            beanDefinition.setScope(SINGLETON);
        }

        beanDefinitionMap.put(beanName,beanDefinition);
    }

    public Object getBean(String beanName) {
        Object bean = singletonBeanMap.get(beanName);
        if (bean == null) {
            bean = createBean(beanName);
        }
        return bean;
    }

    /**
     * 创建bean
     *
     * @param beanName
     * @return
     */
    private Object createBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException("没有找到"+ beanName);
        }
        try {
            Class clazz = beanDefinition.getType();
            // 获取无参构造函数
            Constructor constructor = clazz.getConstructor();
            Object bean = constructor.newInstance();

            // 依赖注入
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    String name = field.getName();
                    Object fieldBean = getBean(name);
                    field.set(bean,fieldBean);
                    field.setAccessible(false);
                }
            }
            return bean;
        } catch (NoSuchMethodException | InvocationTargetException
                | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
