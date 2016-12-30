package io.github.phantamanta44.rphud.util.render;

@FunctionalInterface
public interface ITextureDataRenderer {

    void render(int x, int y, int w, int h, TextureData tex);

}
