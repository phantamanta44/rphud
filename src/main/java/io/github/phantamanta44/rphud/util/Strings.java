package io.github.phantamanta44.rphud.util;

public class Strings {

    public static String format(String format, Object... args) {
        String str = format;
        int ind, i = 0;
        while ((ind = str.indexOf("{}")) != -1 && i < args.length) {
            str = str.substring(0, ind)
                    .concat(stringify(args[i++]))
                    .concat(str.substring(ind + 2, str.length()));
        }
        return str;
    }

    public static String stringify(Object o) {
        return o == null ? "<null>" : o.toString();
    }

}
