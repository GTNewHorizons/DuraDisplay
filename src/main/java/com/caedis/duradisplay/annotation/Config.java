package com.caedis.duradisplay.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
/*
 * Annotation to mark a class as a config class.
 * The class have to contain
 * a public final static String field named category
 * a public final static Self-typed field named instance for Singleton
 */
public @interface Config {

}
