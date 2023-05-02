package koyami.nekochan.commands.control;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface CommandHandle {
    String MOD_CATEGORY = "Moderation";
    String ANIME_CATEGORY = "Anime";
    String RANDOM_CATEGORY = "Random";

    String name();
    String description() default "";
    String category() default "#none";
    String args() default "";
    boolean hidden() default false;
}
