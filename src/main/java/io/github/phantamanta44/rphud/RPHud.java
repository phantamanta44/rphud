package io.github.phantamanta44.rphud;

import io.github.phantamanta44.rphud.event.HudInterceptor;
import io.github.phantamanta44.rphud.event.ReloadHandler;
import io.github.phantamanta44.rphud.hud.HudManager;
import io.github.phantamanta44.rphud.util.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = RPHConst.MOD_ID, version = RPHConst.VERSION)
public class RPHud {

    @Mod.Instance(RPHConst.MOD_ID)
    public static RPHud INSTANCE;

    private final HudManager renderer = new HudManager();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        info("Registering resource manager reload hook...");
        IReloadableResourceManager resMan = (IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager();
        resMan.registerReloadListener(new ReloadHandler());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        info("Registering HUD hook...");
        MinecraftForge.EVENT_BUS.register(new HudInterceptor());
    }

    public HudManager getRenderer() {
        return renderer;
    }

    private static final Logger logger = LogManager.getLogger(RPHConst.MOD_ID);

    public static Logger getLogger() {
        return logger;
    }

    public static void info(String format, Object... args) {
        logger.info(Strings.format(format, args));
    }

    public static void warn(String format, Object... args) {
        logger.warn(Strings.format(format, args));
    }

    public static void warn(Exception e) {
        logger.warn("", e);
    }

    public static void error(String format, Object... args) {
        logger.error(Strings.format(format, args));
    }

    public static void error(Exception e) {
        logger.error("", e);
    }

}
