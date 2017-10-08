package io.github.phantamanta44.rphud.hud;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.FoodStats;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

public class ExprContext {

    public final GuiIngame gui;
    public final EntityPlayer pl;
    public final ScaledResolution res;
    protected final Map<String, DoubleSupplier> vars;

    public ExprContext(GuiIngame gui, EntityPlayer pl, ScaledResolution res) {
        this.gui = gui;
        this.pl = pl;
        this.res = res;
        this.vars = new HashMap<>();
        vars.put("systime", System::currentTimeMillis);
        vars.put("time", pl.getEntityWorld()::getWorldTime);
        vars.put("daytime", () -> pl.getEntityWorld().getWorldTime() % 24000L);
        vars.put("vp_width", res::getScaledWidth);
        vars.put("vp_height", res::getScaledHeight);
        vars.put("hp", pl::getHealth);
        vars.put("hp_max", pl::getMaxHealth);
        vars.put("xp", () -> pl.experience);
        vars.put("level", () -> pl.experienceLevel);
        vars.put("score", pl::getScore);
        vars.put("x", () -> pl.posX);
        vars.put("y", () -> pl.posY);
        vars.put("z", () -> pl.posZ);
        vars.put("vel_x", () -> pl.motionX);
        vars.put("vel_y", () -> pl.motionY);
        vars.put("vel_z", () -> pl.motionZ);
        vars.put("sneaking", () -> pl.isSneaking() ? 1D : 0D);
        vars.put("sprinting", () -> pl.isSprinting() ? 1D : 0D);
        vars.put("pitch", () -> pl.rotationPitch);
        vars.put("yaw", () -> pl.rotationYaw);
        vars.put("angle", () -> (630D - (pl.rotationYaw % 360D)) % 360D);
        FoodStats fs = pl.getFoodStats();
        vars.put("food", fs::getFoodLevel);
        vars.put("food_max", () -> 20D);
        vars.put("sat", fs::getSaturationLevel);
        vars.put("held_item", () -> pl.getHeldItem() != null ? Item.getIdFromItem(pl.getHeldItem().getItem()) : 0);
        vars.put("held_meta", () -> pl.getHeldItem() != null ? pl.getHeldItem().getItemDamage() : 0);
        vars.put("held_count", () -> pl.getHeldItem() != null ? pl.getHeldItem().stackSize : 0D);
    }

}
