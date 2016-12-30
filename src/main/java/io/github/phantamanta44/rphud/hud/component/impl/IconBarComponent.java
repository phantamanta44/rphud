package io.github.phantamanta44.rphud.hud.component.impl;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.hud.component.AbstractComponent;
import io.github.phantamanta44.rphud.util.*;
import io.github.phantamanta44.rphud.util.math.OrthoDir;
import io.github.phantamanta44.rphud.util.math.OrthoDirOffset;
import io.github.phantamanta44.rphud.util.math.ScreenAlign;
import io.github.phantamanta44.rphud.util.render.ITextureDataRenderer;
import io.github.phantamanta44.rphud.util.render.Renders;
import io.github.phantamanta44.rphud.util.render.TextureData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public class IconBarComponent extends AbstractComponent {
    
    private final ScreenAlign align;
    private final OrthoDir dir;
    private final String xExpr, yExpr, wExpr, hExpr, aExpr;
    private final boolean halfVals, showEmpty;
    private final String fullUExpr, fullVExpr, fullSwExpr, fullShExpr;
    private final String halfUExpr, halfVExpr, halfSwExpr, halfShExpr;
    private final String emptyUExpr, emptyVExpr, emptySwExpr, emptyShExpr;
    private final ResourceLocation resource;
    private final int texWidth, texHeight;
    private final String dsExpr, dmExpr, icExpr, spExpr;
    
    public IconBarComponent(DeserializingMap cfg) {
        super(cfg);
        this.align = cfg.has("align") ? ScreenAlign.parse(cfg.getString("align")) : ScreenAlign.TOP_LEFT;
        this.dir = cfg.has("dir") ? OrthoDir.parse(cfg.getString("dir")) : OrthoDir.RIGHT;
        this.xExpr = cfg.getString("x");
        this.yExpr = cfg.getString("y");
        this.wExpr = cfg.getString("width");
        this.hExpr = cfg.getString("height");
        this.aExpr = cfg.get("opacity");
        this.fullUExpr = cfg.getString("fullu");
        this.fullVExpr = cfg.getString("fullv");
        this.fullSwExpr = cfg.getString("fullsectionwidth");
        this.fullShExpr = cfg.getString("fullsectionheight");
        if (this.halfVals = cfg.has("halfvalues") && cfg.getBool("halfvalues")) {
            this.halfUExpr = cfg.getString("halfu");
            this.halfVExpr = cfg.getString("halfv");
            this.halfSwExpr = cfg.getString("halfsectionwidth");
            this.halfShExpr = cfg.getString("halfsectionheight");
        } else {
            this.halfUExpr = this.halfVExpr = this.halfSwExpr = this.halfShExpr = null;
        }
        if (this.showEmpty = cfg.has("showempty") && cfg.getBool("showempty")) {
            this.emptyUExpr = cfg.getString("emptyu");
            this.emptyVExpr = cfg.getString("emptyv");
            this.emptySwExpr = cfg.getString("emptysectionwidth");
            this.emptyShExpr = cfg.getString("emptysectionheight");
        } else {
            this.emptyUExpr = this.emptyVExpr = this.emptySwExpr = this.emptyShExpr = null;
        }
        this.resource = new ResourceLocation(cfg.getString("source"));
        this.texWidth = cfg.getInt("texturewidth");
        this.texHeight = cfg.getInt("textureheight");
        this.dsExpr = cfg.getString("data");
        this.dmExpr = cfg.getString("datamax");
        this.icExpr = cfg.getString("count");
        this.spExpr = cfg.getString("spacing");
    }

    @Override
    public void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        int x = eval.evalInt(xExpr);
        int y = eval.evalInt(yExpr);
        int w = eval.evalInt(wExpr);
        int h = eval.evalInt(hExpr);
        float a = aExpr != null ? eval.evalFloat(aExpr) : 1F;
        TextureData texF = new TextureData(
                eval.evalInt(fullUExpr), eval.evalInt(fullVExpr),
                eval.evalInt(fullSwExpr), eval.evalInt(fullShExpr),
                texWidth, texHeight
        );
        TextureData texE = null;
        if (showEmpty) {
            texE = new TextureData(
                    eval.evalInt(emptyUExpr), eval.evalInt(emptyVExpr),
                    eval.evalInt(emptySwExpr), eval.evalInt(emptyShExpr),
                    texWidth, texHeight
            );
        }
        double val = eval.eval(dsExpr) / eval.eval(dmExpr);
        int count = eval.evalInt(icExpr);
        int space = eval.evalInt(spExpr);
        OrthoDirOffset offset = new OrthoDirOffset(w, h, space);
        x = align.offsetX(x, offset.offsetX(x, dir, count) + w, res.getScaledWidth());
        y = align.offsetY(y, offset.offsetY(y, dir, count) + h, res.getScaledHeight());
        Supplier<Pair<Integer, Integer>> coordSupplier = offset.offsets(x, y, dir);
        mc.renderEngine.bindTexture(resource);
        ITextureDataRenderer renderer = underHud ? Renders::textureUnderHud : Renders::textureOverHud;
        Renders.alpha(a);
        if (halfVals) {
            TextureData texH = new TextureData(
                    eval.evalInt(halfUExpr), eval.evalInt(halfVExpr),
                    eval.evalInt(halfSwExpr), eval.evalInt(halfShExpr),
                    texWidth, texHeight
            );
            for (int i = 0; i < count; i++) {
                double t1 = (double)(i * 2 + 1) / (count * 2);
                double t2 = (double)(i * 2 + 2) / (count * 2);
                Pair<Integer, Integer> coords = coordSupplier.get();
                if (val >= t2)
                    renderer.render(coords.getLeft(), coords.getRight(), w, h, texF);
                else if (val >= t1)
                    renderer.render(coords.getLeft(), coords.getRight(), w, h, texH);
                else if (showEmpty)
                    renderer.render(coords.getLeft(), coords.getRight(), w, h, texE);
            }
        } else {
            for (int i = 0; i < count; i++) {
                double thresh = (double)(i + 1) / count;
                Pair<Integer, Integer> coords = coordSupplier.get();
                if (val >= thresh)
                    renderer.render(coords.getLeft(), coords.getRight(), w, h, texF);
                else if (showEmpty)
                    renderer.render(coords.getLeft(), coords.getRight(), w, h, texE);
            }
        }
        Renders.alpha(1F);
    }
    
}
