package io.github.phantamanta44.rphud.hud.cancel;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class PermanentCancel implements ICancel {

    private final String name;

    public PermanentCancel(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean shouldCancel(Minecraft mc, ScaledResolution sr, ExpressionEngine eval) {
        return false;
    }

}
