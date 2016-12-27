package io.github.phantamanta44.rphud.hud.component;

import io.github.phantamanta44.rphud.hud.ExpressionEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public interface IComponent {

    void render(Minecraft mc, ScaledResolution res, ExpressionEngine eval);

    boolean renderUnderHud();

}
