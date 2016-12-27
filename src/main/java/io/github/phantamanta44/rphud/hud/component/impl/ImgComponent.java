package io.github.phantamanta44.rphud.hud.component.impl;

import io.github.phantamanta44.rphud.hud.ExprContext;
import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.hud.component.IComponent;
import io.github.phantamanta44.rphud.util.DeserializingMap;
import io.github.phantamanta44.rphud.util.ScreenAlign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ImgComponent implements IComponent {

    private final ScreenAlign align;
    private final String xExpr, yExpr, wExpr, hExpr, uExpr, vExpr, twExpr, thExpr, aExpr;
    private final ResourceLocation resource;

    public ImgComponent(DeserializingMap cfg) {
        this.align = cfg.has("align") ? ScreenAlign.parse(cfg.getString("align")) : ScreenAlign.TOP_LEFT;
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.wExpr = cfg.getString("width");
        this.hExpr = cfg.getString("height");
        this.uExpr = cfg.get("u");
        this.vExpr = cfg.get("v");
        this.twExpr = cfg.get("texturewidth");
        this.thExpr = cfg.get("textureheight");
        this.aExpr = cfg.get("opacity");
        this.resource = new ResourceLocation(cfg.getString("source"));
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        eval.context(new ExprContext(mc.ingameGUI, mc.thePlayer, res));
        int x = eval.evalInt(xExpr);
        int y = eval.evalInt(yExpr);
        int w = eval.evalInt(wExpr);
        int h = eval.evalInt(hExpr);
        int u = uExpr != null ? eval.evalInt(uExpr) : 0;
        int v = vExpr != null ? eval.evalInt(vExpr) : 0;
        int tw = twExpr != null ? eval.evalInt(twExpr) : w;
        int th = thExpr != null ? eval.evalInt(thExpr) : h;
        float a = aExpr != null ? eval.evalFloat(aExpr) : 1;
        eval.exitContext();
        x = align.offsetX(x, w, res.getScaledWidth());
        y = align.offsetY(y, h, res.getScaledHeight());
        mc.renderEngine.bindTexture(resource);
        GlStateManager.color(1F, 1F, 1F, a);
        Gui.drawModalRectWithCustomSizedTexture(x, y, u, v, w, h, tw, th);
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

}
