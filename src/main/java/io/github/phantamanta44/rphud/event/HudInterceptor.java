package io.github.phantamanta44.rphud.event;

import io.github.phantamanta44.rphud.RPHud;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class HudInterceptor {

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (RPHud.INSTANCE.getRenderer().shouldCancel(event.type.toString()))
            event.setCanceled(true);
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR)
            RPHud.INSTANCE.getRenderer().doRender(false);
    }

    @SubscribeEvent
    public void onRender(RenderHandEvent event) {
        if (RPHud.INSTANCE.getRenderer().shouldCancel("hand"))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderDone(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT)
            RPHud.INSTANCE.getRenderer().doRender(true);
    }

}
