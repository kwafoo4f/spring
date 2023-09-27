package cpm.kuafoo4j.spring.ext;

/**
 * 初始化bean
 */
public interface InitializingBean {
    /**
     * 属性设置后
     */
    void afterPropertiesSet();
}
