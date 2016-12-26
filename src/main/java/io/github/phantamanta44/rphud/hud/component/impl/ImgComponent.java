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
    private final String xExpr, yExpr, wExpr, hExpr;
    private final ResourceLocation resource;

    public ImgComponent(DeserializingMap cfg) {
        this.align = ScreenAlign.parse(cfg.getString("align"));
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.wExpr = cfg.getString("width");
        this.hExpr = cfg.getString("height");
        this.resource = new ResourceLocation(cfg.getString("source"));
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        eval.context(new ExprContext(mc.ingameGUI, mc.thePlayer, res));
        int x = eval.evalInt(xExpr);
        int y = eval.evalInt(yExpr);
        int w = eval.evalInt(wExpr);
        int h = eval.evalInt(hExpr);
        eval.exitContext();
        x = align.offsetX(x, w, res.getScaledWidth());
        y = align.offsetY(y, h, res.getScaledHeight());
        mc.renderEngine.bindTexture(resource);
        GlStateManager.color(1F, 1F, 1F);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, w, h, w, h);
    }

}
