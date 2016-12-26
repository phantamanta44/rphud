package io.github.phantamanta44.rphud.util;

import java.util.HashMap;
import java.util.Map;

public enum ScreenAlign {

    TOP_LEFT((x, w, s) -> x, (y, h, s) -> y,
            "topleft", "lefttop",
            "upleft", "leftup",
            "upperleft"),
    TOP((x, w, s) -> x + s / 2 - w / 2, (y, h, s) -> y,
            "top", "up", "upper",
            "topcenter", "centertop",
            "topcentre", "centretop",
            "topmiddle", "middletop"),
    TOP_RIGHT((x, w, s) -> s - w - x, (y, h, s) -> y,
            "topright", "righttop",
            "upright", "rightup",
            "upperright"),
    LEFT((x, w, s) -> x, (y, h, s) -> y + s / 2 - h / 2,
            "left",
            "leftcenter", "centerleft",
            "leftcentre", "centreleft",
            "leftmiddle", "middleleft"),
    CENTER((x, w, s) -> x + s / 2 - w / 2, (y, h, s) -> y + s / 2 - h / 2,
            "middle", "center", "centre"),
    RIGHT((x, w, s) -> s - w - x, (y, h, s) -> y + s / 2 - h / 2,
            "right",
            "rightcenter", "centerright",
            "rightcentre", "centreright",
            "rightmiddle", "middleright"),
    BOTTOM_LEFT((x, w, s) -> x, (y, h, s) -> s - h - y,
            "bottomleft", "leftbottom",
            "downleft", "leftdown",
            "lowerleft"),
    BOTTOM((x, w, s) -> x + s / 2 - w / 2, (y, h, s) -> s - h - y,
            "bottom", "down", "lower",
            "bottomcenter", "centerbottom",
            "bottomcentre", "centrebottom",
            "bottommiddle", "middlebottom"),
    BOTTOM_RIGHT((x, w, s) -> s - w - x, (y, h, s) -> s - h - y,
            "bottomright", "rightbottom",
            "downright", "rightdown",
            "lowerright");

    private static final Map<String, ScreenAlign> alignments = new HashMap<>();

    static {
        for (ScreenAlign align : values()) {
            for (String name : align.names)
                alignments.put(name, align);
        }
    }

    private final OffsetOp x, y;
    private final String[] names;

    ScreenAlign(OffsetOp x, OffsetOp y, String... names) {
        this.x = x;
        this.y = y;
        this.names = names;
    }

    public int offsetX(int x, int width, int screen) {
        return this.x.offset(x, width, screen);
    }

    public int offsetY(int y, int height, int screen) {
        return this.y.offset(y, height, screen);
    }

    public static ScreenAlign parse(String name) {
        return alignments.get(name.replaceAll("[-_\\s]+", "").toLowerCase());
    }
    
    @FunctionalInterface
    private interface OffsetOp {
        
        int offset(int pos, int dim, int screen);
        
    }

}
