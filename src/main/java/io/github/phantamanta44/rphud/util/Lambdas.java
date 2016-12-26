package io.github.phantamanta44.rphud.util;

import java.util.function.Function;

public class Lambdas {

    public static <A> Function<A, A> identity() {
        return a -> a;
    }

}
