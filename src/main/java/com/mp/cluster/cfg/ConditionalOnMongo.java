package com.mp.cluster.cfg;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnProperty(
        prefix = "cluster-sync",
        name = "data-source",
        havingValue = "mongo"
)
public @interface ConditionalOnMongo {
}
