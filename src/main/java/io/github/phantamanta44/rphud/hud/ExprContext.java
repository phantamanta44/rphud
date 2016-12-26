package io.github.phantamanta44.rphud.hud;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

public class ExprContext {

    protected final Map<String, DoubleSupplier> vars;

    public ExprContext(GuiIngame gui, EntityPlayer pl, ScaledResolution res) {
        vars = new HashMap<>();
        vars.put("systime", System::currentTimeMillis);
        vars.put("time", pl.getEntityWorld()::getWorldTime);
        vars.put("daytime", () -> pl.getEntityWorld().getWorldTime() % 24000L);
        vars.put("hp", pl::getHealth);
        vars.put("hpmax", pl::getMaxHealth);
        vars.put("xp", () -> pl.experience);
        vars.put("level", () -> pl.experienceLevel);
        vars.put("score", pl::getScore);
        FoodStats fs = pl.getFoodStats();
        vars.put("hunger", fs::getFoodLevel);
        vars.put("sat", fs::getSaturationLevel);
    }

}
