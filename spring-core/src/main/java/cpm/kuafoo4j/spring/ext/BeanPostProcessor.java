package cpm.kuafoo4j.spring.ext;

/**
 * bean后置处理器
 */
public interface BeanPostProcessor {

    /**
     * 初始化前
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 初始化后
     *
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
