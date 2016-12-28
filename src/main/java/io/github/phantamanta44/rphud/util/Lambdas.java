package io.github.phantamanta44.rphud.util;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Lambdas {

    public static <A> Function<A, A> identity() {
        return a -> a;
    }

}
