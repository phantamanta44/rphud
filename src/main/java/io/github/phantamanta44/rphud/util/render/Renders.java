package io.github.phantamanta44.rphud.util.render;

import io.github.phantamanta44.rphud.RPHud;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Renders {

    public static void alpha(float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.color(1F, 1F, 1F, alpha);
    }

    public static void alphaOff() {
        GlStateManager.disableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    public static void textureOverHud(int x, int y, int w, int h, TextureData tex) {
        textureOverHud(x, y, tex.u, tex.v, w, h, tex.sw, tex.sh, tex.tw, tex.th);
    }

    public static void textureOverHud(int x, int y, int u, int v, int w, int h, int tw, int th) {
        textureOverHud(x, y, u, v, w, h, w, h, tw, th);
    }

    public static void textureOverHud(int x, int y, int u, int v, int w, int h, int sw, int sh, int tw, int th) {
        texture(x, y, u, v, w, h, sw, sh, tw, th, 0);
    }

    public static void textureUnderHud(int x, int y, int w, int h, TextureData tex) {
        textureUnderHud(x, y, tex.u, tex.v, w, h, tex.sw, tex.sh, tex.tw, tex.th);
    }

    public static void textureUnderHud(int x, int y, int u, int v, int w, int h, int tw, int th) {
        textureUnderHud(x, y, u, v, w, h, w, h, tw, th);
    }

    public static void textureUnderHud(int x, int y, int u, int v, int w, int h, int sw, int sh, int tw, int th) {
        texture(x, y, u, v, w, h, sw, sh, tw, th, -100);
    }

    public static void texture(int x, int y, int w, int h, TextureData tex, double z) {
        texture(x, y, tex.u, tex.v, w, h, tex.sw, tex.sh, tex.tw, tex.th, z);
    }
    
    public static void texture(int x, int y, int u, int v, int w, int h, int tw, int th, double z) {
        texture(x, y, u, v, w, h, w, h, tw, th, z);
    }

    public static void texture(int x, int y, int u, int v, int w, int h, int sw, int sh, int tw, int th, double z) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION_TEX);
        wr.pos(x    , y + h, z).tex((double) u / tw      , (double)(v + sh) / th).endVertex();
        wr.pos(x + w, y + h, z).tex((double)(u + sw) / tw, (double)(v + sh) / th).endVertex();
        wr.pos(x + w, y    , z).tex((double)(u + sw) / tw, (double) v / th      ).endVertex();
        wr.pos(x    , y    , z).tex((double) u / tw      , (double) v / th      ).endVertex();
        tess.draw();
    }

    public static void rectOverHud(int x, int y, int w, int h, int colour) {
        rect(x, y, w, h, colour, 0);
    }

    public static void rectUnderHud(int x, int y, int w, int h, int colour) {
        rect(x, y, w, h, colour, -100);
    }

    public static void rect(int x, int y, int w, int h, int colour, double z) {
        int r = (colour >> 16) & 255;
        int g = (colour >> 8) & 255;
        int b = colour & 255;
        int a = (colour >> 24) & 255;
        GlStateManager.disableTexture2D();
        GlStateManager.color(r / 255F, g / 255F, b / 255F, a / 255F);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(7, DefaultVertexFormats.POSITION);
        wr.pos(x    , y + h, z).endVertex();
        wr.pos(x + w, y + h, z).endVertex();
        wr.pos(x + w, y    , z).endVertex();
        wr.pos(x    , y    , z).endVertex();
        tess.draw();
        alpha(1F);
        GlStateManager.enableTexture2D();
    }

}
