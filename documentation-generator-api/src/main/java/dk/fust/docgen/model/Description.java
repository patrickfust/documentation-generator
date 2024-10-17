package dk.fust.docgen.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that are used for documenting the model.
 * This way we can get a json-schema with more information.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Description {

    /**
     * Description of the element of the model
     * @return the description
     */
    String value();

    /**
     * Does the field have a default boolean
     * @return true if you should check `defaultBoolean`
     */
    boolean hasDefaultBoolean() default false;

    /**
     * If `hasDefaultBoolean` is true, this field tells wether or not the default
     * boolean value of the field
     * @return default value of the field
     */
    boolean defaultBoolean() default false;

    /**
     * Wether or not the field is required
     * @return true if the field is required
     */
    boolean required() default false;

    /**
     * If not "", this will be the default value of the field
     * @return default value
     */
    String defaultValue() default "";
}
