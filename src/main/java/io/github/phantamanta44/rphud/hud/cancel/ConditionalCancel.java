package io.github.phantamanta44.rphud.hud.cancel;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import io.github.phantamanta44.rphud.util.math.Floats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ConditionalCancel implements ICancel {

    private final String name, condition;

    public ConditionalCancel(String name, String condition) {
        this.name = name;
        this.condition = condition;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean shouldCancel(Minecraft mc, ScaledResolution sr, ExpressionEngine eval) {
        return Floats.isNonZero(eval.eval(condition));
    }

}
