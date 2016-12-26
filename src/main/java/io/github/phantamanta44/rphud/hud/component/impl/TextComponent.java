package io.github.phantamanta44.rphud.hud.component.impl;

import io.github.phantamanta44.rphud.hud.ExprContext;
import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.hud.component.IComponent;
import io.github.phantamanta44.rphud.util.DeserializingMap;
import io.github.phantamanta44.rphud.util.ScreenAlign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class TextComponent implements IComponent {

    private final ScreenAlign align;
    private final String textExpr, xExpr, yExpr, colExpr;

    public TextComponent(DeserializingMap cfg) {
        this.align = ScreenAlign.parse(cfg.getString("align"));
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.textExpr = cfg.getString("text");
        this.colExpr = cfg.getString("colour");
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        ExprContext ctx = new ExprContext(mc.ingameGUI, mc.thePlayer, res);
        int x = eval.evaluateInt(xExpr, ctx);
        int y = eval.evaluateInt(yExpr, ctx);
        String text = eval.formatStr(textExpr, ctx);
        x = align.offsetX(x, mc.fontRendererObj.getStringWidth(text), res.getScaledWidth());
        y = align.offsetY(y, mc.fontRendererObj.FONT_HEIGHT, res.getScaledHeight());
        int colour = eval.evaluateInt(colExpr, ctx);
        mc.fontRendererObj.drawStringWithShadow(text, x, y, colour);
    }

}
