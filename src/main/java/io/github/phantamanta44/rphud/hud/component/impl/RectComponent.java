package io.github.phantamanta44.rphud.hud.component.impl;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.hud.component.AbstractComponent;
import io.github.phantamanta44.rphud.util.DeserializingMap;
import io.github.phantamanta44.rphud.util.math.ScreenAlign;
import io.github.phantamanta44.rphud.util.render.Renders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class RectComponent extends AbstractComponent {

    private final ScreenAlign align;
    private final String xExpr, yExpr, wExpr, hExpr, colExpr;

    public RectComponent(DeserializingMap cfg) {
        super(cfg);
        this.align = cfg.has("align") ? ScreenAlign.parse(cfg.getString("align")) : ScreenAlign.TOP_LEFT;
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.wExpr = cfg.getString("width");
        this.hExpr = cfg.getString("height");
        this.colExpr = cfg.getString("colour");
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        int x = eval.evalInt(xExpr);
        int y = eval.evalInt(yExpr);
        int w = eval.evalInt(wExpr);
        int h = eval.evalInt(hExpr);
        int colour = eval.evalInt(colExpr);
        x = align.offsetX(x, w, res.getScaledWidth());
        y = align.offsetY(y, h, res.getScaledHeight());
        if (underHud)
            Renders.rectUnderHud(x, y, w, h, colour);
        else
            Renders.rectOverHud(x, y, w, h, colour);
    }

}
