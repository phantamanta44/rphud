package io.github.phantamanta44.rphud.hud.component;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.util.DeserializingMap;
import io.github.phantamanta44.rphud.util.math.Floats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public abstract class AbstractComponent implements IComponent {

    protected final boolean underHud;
    protected final String renderIf;

    public AbstractComponent(DeserializingMap cfg) {
        this.underHud = cfg.has("underhud") && cfg.getBool("underhud");
        this.renderIf = cfg.get("renderif");
    }

    @Override
    public boolean renderUnderHud() {
        return underHud;
    }

    @Override
    public boolean shouldRender(Minecraft mc, ScaledResolution res, ExpressionEngine eval) {
        return renderIf == null || Floats.isNonZero(eval.eval(renderIf));
    }

}
