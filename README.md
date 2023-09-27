# spring
spring核心


加载：
- 1.扫描
- 2.封装beanDefinition
- 3.创建非懒加载的单例bean
  - 3.1.创建前拓展: bean后置处理器-BeanPostProcessor
  - 3.1.创建后拓展:初始化Bean-InitializingBean
  - spring-Aware回调