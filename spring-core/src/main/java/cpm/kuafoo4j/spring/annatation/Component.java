package cpm.kuafoo4j.spring.annatation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    /**
     * beanname
     * @return
     */
    String value() default "";
}
