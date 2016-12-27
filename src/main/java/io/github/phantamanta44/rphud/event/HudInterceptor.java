package io.github.phantamanta44.rphud.event;

import io.github.phantamanta44.rphud.RPHud;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HudInterceptor {

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (RPHud.INSTANCE.getRenderer().shouldCancel(event.getType()))
            event.setCanceled(true);
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
            RPHud.INSTANCE.getRenderer().doRender(false);
        else if (event.getType() == RenderGameOverlayEvent.ElementType.SUBTITLES)
            RPHud.INSTANCE.getRenderer().doRender(true);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {

    }

}
