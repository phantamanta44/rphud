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
        this.align = cfg.has("align") ? ScreenAlign.parse(cfg.getString("align")) : ScreenAlign.TOP_LEFT;
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.wExpr = cfg.getString("width");
        this.hExpr = cfg.getString("height");
        this.colExpr = cfg.getString("colour");
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        eval.context(new ExprContext(mc.ingameGUI, mc.thePlayer, res));
        int x = eval.evalInt(xExpr);
        int y = eval.evalInt(yExpr);
        int w = eval.evalInt(wExpr);
        int h = eval.evalInt(hExpr);
        int colour = eval.evalInt(colExpr);
        eval.exitContext();
        x = align.offsetX(x, w, res.getScaledWidth());
        y = align.offsetY(y, h, res.getScaledHeight());
        Gui.drawRect(x, y, x + w, y + h, colour);
    }

}
