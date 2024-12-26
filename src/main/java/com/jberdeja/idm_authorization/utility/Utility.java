package com.jberdeja.idm_authorization.utility;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utility {
    public static <T> void validate(T value, Predicate<T> validator, String errorMessage) {
        if (validator.test(value)) {
            log.error(errorMessage + ": '{}'", value);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static <T> void validate(Supplier<Boolean> condition, String errorMessage) {
        if (condition.get()) {
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    public static <T, M> void validate(T v1, M v2, BiPredicate<T, M> validator, String errorMessage) {
        if (validator.test(v1, v2)) {
            log.error(errorMessage + ": '{}' and '{}'", v1, v2);
            throw new IllegalArgumentException(errorMessage);
        }
    }
    

    public static boolean isNotNullOrBlank(String value){
        return ! isNullOrBlank(value);
    }

    public static boolean isNullOrBlank(String value){
        return Objects.isNull(value) || String.valueOf(value).isBlank();
    }

    public static boolean isNotEquals(String a, String b){
        return ! a.equalsIgnoreCase(b);
    }

    public static boolean startsWith(String a, String b){
        return a.startsWith(b);
    }

    public static <T> boolean isNullObject(T object){
        return ! Optional.ofNullable(object).isPresent();
    }

    public static <T> boolean isNotNullObject(T object){
        return ! isNullObject(object);
    }
}
