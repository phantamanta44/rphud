package io.github.phantamanta44.rphud.hud.cancel;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public interface ICancel {

    String getName();

    boolean shouldCancel(Minecraft mc, ScaledResolution sr, ExpressionEngine eval);

}
