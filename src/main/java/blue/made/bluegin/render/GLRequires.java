package blue.made.bluegin.render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates any feature of the library that requires OpenGL 3.0 or greater
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.PACKAGE, ElementType.FIELD})
public @interface GLRequires {
    public int major();
    public int minor();
}
