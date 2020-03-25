package xyz.brettb.ac.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CorePluginCommandMeta {
    String description() default "A command.";
    String[] aliases() default {};
    String usage() default "";
}
