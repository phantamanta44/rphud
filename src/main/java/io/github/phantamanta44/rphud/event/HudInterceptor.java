package io.github.phantamanta44.rphud.event;

import io.github.phantamanta44.rphud.RPHud;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HudInterceptor {

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (RPHud.INSTANCE.getRenderer().shouldCancel(event.type))
            event.setCanceled(true);
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR)
            RPHud.INSTANCE.getRenderer().doRender(false);
    }

    @SubscribeEvent
    public void onRenderDone(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT)
            RPHud.INSTANCE.getRenderer().doRender(true);
    }

}
