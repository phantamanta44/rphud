package io.github.phantamanta44.rphud.util;

import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public class OrthoDirOffset {

    private final int oX, oY;

    public OrthoDirOffset(int dims, int spacing) {
        this(dims, dims, spacing);
    }

    public OrthoDirOffset(int width, int height, int spacing) {
        this(width, height, spacing, spacing);
    }

    public OrthoDirOffset(int width, int height, int spacingX, int spacingY) {
        this.oX = width + spacingX;
        this.oY = height + spacingY;
    }

    public int offsetX(int x, OrthoDir dir, int index) {
        return x + dir.x * oX * index;
    }

    public int offsetY(int y, OrthoDir dir, int index) {
        return y + dir.y * oY * index;
    }

    public Supplier<Pair<Integer, Integer>> offsets(int x, int y, OrthoDir dir) {
        return new OffsetSupplier(this, x, y, dir);
    }

    private static class OffsetSupplier implements Supplier<Pair<Integer, Integer>> {

        private final OrthoDirOffset offset;
        private final int x, y;
        private final OrthoDir dir;
        private int index;

        OffsetSupplier(OrthoDirOffset offset, int x, int y, OrthoDir dir) {
            this.offset = offset;
            this.x = x;
            this.y = y;
            this.dir = dir;
            index = -1;
        }

        @Override
        public Pair<Integer, Integer> get() {
            index++;
            return Pair.of(offset.offsetX(x, dir, index), offset.offsetY(y, dir, index));
        }

    }

}
