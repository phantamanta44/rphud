package io.github.phantamanta44.rphud.event;

import io.github.phantamanta44.rphud.RPHConst;
import io.github.phantamanta44.rphud.RPHud;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReloadHandler implements IResourceManagerReloadListener {

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager man) {
        try {
            RPHud.info("Resources reloaded! Rebuilding HUD components...");
            IResource cfg = man.getResource(RPHConst.PACK_CFG_FILE);
            try (BufferedInputStream in = new BufferedInputStream(cfg.getInputStream())) {
                RPHud.INSTANCE.getRenderer().rebuild(IOUtils.readLines(in));
            }
        } catch (FileNotFoundException e) {
            RPHud.info("No RPHud configuration file found; reverting to vanilla HUD.");
            RPHud.INSTANCE.getRenderer().clearCache();
        } catch (IOException e) {
            RPHud.error("Failed to rebuild configuration file!");
            RPHud.getLogger().error(e);
        }
    }

}
