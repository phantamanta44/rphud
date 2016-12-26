package io.github.phantamanta44.rphud.hud.component.impl;

import io.github.phantamanta44.rphud.hud.ExprContext;
import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.hud.component.IComponent;
import io.github.phantamanta44.rphud.util.DeserializingMap;
import io.github.phantamanta44.rphud.util.ScreenAlign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class RectComponent implements IComponent {

    private final ScreenAlign align;
    private final String xExpr, yExpr, wExpr, hExpr, colExpr;

    public RectComponent(DeserializingMap cfg) {
        this.align = ScreenAlign.parse(cfg.getString("align"));
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.wExpr = cfg.getString("width");
        this.hExpr = cfg.getString("height");
        this.colExpr = cfg.getString("colour");
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        ExprContext ctx = new ExprContext(mc.ingameGUI, mc.thePlayer, res);
        int x = eval.evaluateInt(xExpr, ctx);
        int y = eval.evaluateInt(yExpr, ctx);
        int w = eval.evaluateInt(wExpr, ctx);
        int h = eval.evaluateInt(hExpr, ctx);
        x = align.offsetX(x, w, res.getScaledWidth());
        y = align.offsetY(y, h, res.getScaledHeight());
        int colour = eval.evaluateInt(colExpr, ctx);
        Gui.drawRect(x, y, x + w, y + h, colour);
    }

}
