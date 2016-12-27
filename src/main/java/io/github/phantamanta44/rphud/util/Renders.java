package io.github.phantamanta44.rphud.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
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

    public static void textureOverHud(int x, int y, int u, int v, int w, int h, int tw, int th) {
        texture(x, y, u, v, w, h, tw, th, 0);
    }

    public static void textureUnderHud(int x, int y, int u, int v, int w, int h, int tw, int th) {
        texture(x, y, u, v, w, h, tw, th, -100);
    }

    public static void texture(int x, int y, int u, int v, int w, int h, int tw, int th, double z) {
        float f = 1.0F / tw;
        float f1 = 1.0F / th;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexbuffer.pos((double)x, (double)(y + h), z).tex((double)(u * f), (double)((v + (float)h) * f1)).endVertex();
        vertexbuffer.pos((double)(x + w), (double)(y + h), z).tex((double)((u + (float)w) * f), (double)((v + (float)h) * f1)).endVertex();
        vertexbuffer.pos((double)(x + w), (double)y, z).tex((double)((u + (float)w) * f), (double)(v * f1)).endVertex();
        vertexbuffer.pos((double)x, (double)y, z).tex((double)(u * f), (double)(v * f1)).endVertex();
        tessellator.draw();
    }

}
