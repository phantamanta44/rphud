package io.github.phantamanta44.rphud.util.math;

public class Floats {

    private static final float FLOAT_ERROR = 1e-8F;
    private static final double DOUBLE_ERROR = 1e-8D;

    public static boolean isZero(float val) {
        return isEqual(val, 0F);
    }

    public static boolean isZero(double val) {
        return isEqual(val, 0D);
    }

    public static boolean isNonZero(float val) {
        return !isZero(val);
    }

    public static boolean isNonZero(double val) {
        return !isZero(val);
    }

    public static boolean isEqual(float a, float b) {
        return Math.abs(a - b) < FLOAT_ERROR;
    }

    public static boolean isEqual(double a, double b) {
        return Math.abs(a - b) < DOUBLE_ERROR;
    }

    public static boolean isNotEqual(float a, float b) {
        return !isEqual(a, b);
    }

    public static boolean isNotEqual(double a, double b) {
        return !isEqual(a, b);
    }

}
