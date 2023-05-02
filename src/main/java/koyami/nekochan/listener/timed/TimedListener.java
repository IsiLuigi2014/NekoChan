package koyami.nekochan.listener.timed;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TimedListener {
    String name();
    int time() default 60; //60s
    //boolean active() default true;
}
