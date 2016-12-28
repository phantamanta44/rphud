package io.github.phantamanta44.rphud.util;

public class TextureData {

    public final int u, v, sw, sh, tw, th;

    public TextureData(int u, int v, int sectionW, int sectionH, int textureW, int textureH) {
        this.u = u;
        this.v = v;
        this.sw = sectionW;
        this.sh = sectionH;
        this.tw = textureW;
        this.th = textureH;
    }

    public TextureData(int u, int v, int textureW, int textureH) {
        this(u, v, textureW, textureH, textureW, textureH);
    }

}
