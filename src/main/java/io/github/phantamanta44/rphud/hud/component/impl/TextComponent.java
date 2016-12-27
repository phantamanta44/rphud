package io.github.phantamanta44.rphud.hud.component.impl;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.hud.component.AbstractComponent;
import io.github.phantamanta44.rphud.util.DeserializingMap;
import io.github.phantamanta44.rphud.util.ScreenAlign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class TextComponent extends AbstractComponent {

    private final ScreenAlign align;
    private final String textExpr, xExpr, yExpr, colExpr;
    private final boolean shadow;

    public TextComponent(DeserializingMap cfg) {
        super(cfg);
        this.align = cfg.has("align") ? ScreenAlign.parse(cfg.getString("align")) : ScreenAlign.TOP_LEFT;
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.textExpr = cfg.getString("text");
        this.colExpr = cfg.getString("colour");
        this.shadow = !cfg.has("shadow") || cfg.getBool("shadow");
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        int x = eval.evalInt(xExpr);
        int y = eval.evalInt(yExpr);
        String text = eval.formatStr(textExpr);
        int colour = eval.evalInt(colExpr);
        x = align.offsetX(x, mc.fontRendererObj.getStringWidth(text), res.getScaledWidth());
        y = align.offsetY(y, mc.fontRendererObj.FONT_HEIGHT, res.getScaledHeight());
        mc.fontRendererObj.drawString(text, x, y, colour, shadow);
    }

}
