package io.github.phantamanta44.rphud.hud.component.impl;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.hud.component.AbstractComponent;
import io.github.phantamanta44.rphud.util.DeserializingMap;
import io.github.phantamanta44.rphud.util.Renders;
import io.github.phantamanta44.rphud.util.ScreenAlign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class ImgComponent extends AbstractComponent {

    private final ScreenAlign align;
    private final String xExpr, yExpr, wExpr, hExpr, uExpr, vExpr, swExpr, shExpr, aExpr;
    private final ResourceLocation resource;
    private final int texWidth, texHeight;

    public ImgComponent(DeserializingMap cfg) {
        super(cfg);
        this.align = cfg.has("align") ? ScreenAlign.parse(cfg.getString("align")) : ScreenAlign.TOP_LEFT;
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.wExpr = cfg.getString("width");
        this.hExpr = cfg.getString("height");
        this.uExpr = cfg.get("u");
        this.vExpr = cfg.get("v");
        this.swExpr = cfg.get("sectionwidth");
        this.shExpr = cfg.get("sectionheight");
        this.aExpr = cfg.get("opacity");
        this.resource = new ResourceLocation(cfg.getString("source"));
        this.texWidth = cfg.has("texturewidth") ? cfg.getInt("texturewidth") : -1;
        this.texHeight = cfg.has("textureheight") ? cfg.getInt("textureheight") : -1;
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        int x = eval.evalInt(xExpr);
        int y = eval.evalInt(yExpr);
        int w = eval.evalInt(wExpr);
        int h = eval.evalInt(hExpr);
        int u = uExpr != null ? eval.evalInt(uExpr) : 0;
        int v = vExpr != null ? eval.evalInt(vExpr) : 0;
        int sw = swExpr != null ? eval.evalInt(swExpr) : w;
        int sh = shExpr != null ? eval.evalInt(shExpr) : h;
        int tw = texWidth != -1 ? texWidth : w;
        int th = texHeight != -1 ? texHeight : h;
        float a = aExpr != null ? eval.evalFloat(aExpr) : 1;
        x = align.offsetX(x, w, res.getScaledWidth());
        y = align.offsetY(y, h, res.getScaledHeight());
        mc.renderEngine.bindTexture(resource);
        Renders.alpha(a);
        Renders.textureUnderHud(x, y, u, v, w, h, sw, sh, tw, th);
        Renders.alpha(1F);
    }

}
