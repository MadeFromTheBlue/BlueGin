package blue.made.bluegin.render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates any feature of the library that requires a specific OpenGL extension
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.PACKAGE, ElementType.FIELD})
public @interface GLExtension {
    public String name();
}
