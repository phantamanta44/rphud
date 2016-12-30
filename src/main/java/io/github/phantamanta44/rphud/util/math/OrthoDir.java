package io.github.phantamanta44.rphud.util.math;

import java.util.HashMap;
import java.util.Map;

public enum OrthoDir {

    UP(0, -1, "up", "north"),
    LEFT(-1, 0, "left", "west"),
    DOWN(0, 1, "down", "south", "column", "col"),
    RIGHT(1, 0, "right", "east", "row");

    private static final Map<String, OrthoDir> dirs = new HashMap<>();

    static {
        for (OrthoDir dir : values()) {
            for (String name : dir.names)
                dirs.put(name, dir);
        }
    }

    public final int x, y;

    private final String[] names;

    OrthoDir(int x, int y, String... names) {
        this.x = x;
        this.y = y;
        this.names = names;
    }

    public static OrthoDir parse(String name) {
        return dirs.get(name.toLowerCase());
    }

}
