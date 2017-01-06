package io.github.phantamanta44.rphud.hud;

import io.github.phantamanta44.rphud.RPHud;
import io.github.phantamanta44.rphud.hud.component.IComponent;
import io.github.phantamanta44.rphud.hud.component.IComponentProvider;
import io.github.phantamanta44.rphud.hud.component.SimpleComponentProvider;
import io.github.phantamanta44.rphud.hud.component.impl.IconBarComponent;
import io.github.phantamanta44.rphud.hud.component.impl.ImgComponent;
import io.github.phantamanta44.rphud.hud.component.impl.RectComponent;
import io.github.phantamanta44.rphud.hud.component.impl.TextComponent;
import io.github.phantamanta44.rphud.util.function.Lambdas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HudManager {

    private static final Map<String, IComponentProvider> providers;

    static {
        providers = Stream.of(
                new SimpleComponentProvider("text", TextComponent::new),
                new SimpleComponentProvider("rect", RectComponent::new),
                new SimpleComponentProvider("img", ImgComponent::new),
                new SimpleComponentProvider("iconbar", IconBarComponent::new)
        ).collect(Collectors.toMap(IComponentProvider::getName, Lambdas.identity()));
    }

    private final Minecraft mc;
    private final Set<RenderGameOverlayEvent.ElementType> toCancel = new HashSet<>();
    private final ExpressionEngine eval = new ExpressionEngine();
    private final List<IComponent> components = new ArrayList<>();
    private boolean failed = false;

    public HudManager() {
        this.mc = Minecraft.getMinecraft();
    }

    public void clearCache() {
        toCancel.clear();
        components.clear();
        eval.exitContext();
        eval.defined.clear();
        eval.register.clear();
        failed = false;
    }

    public void rebuild(List<String> cfgFile) {
        clearCache();
        MutableBoolean failed = new MutableBoolean(false);
        try {
            new HudParser().withDefinitionHandler((n, e) ->
                eval.defined.add(Pair.of(n, e))
            ).withCancelHandler(c -> {
                try {
                    try {
                        toCancel.add(RenderGameOverlayEvent.ElementType.valueOf(c.toUpperCase().replaceAll(" ", "")));
                    } catch (IllegalArgumentException e) {
                        toCancel.add(RenderGameOverlayEvent.ElementType.valueOf(c.toUpperCase().replaceAll(" ", "_")));
                    }
                } catch (Exception e) {
                    RPHud.warn("Errored on cancel directive!");
                    RPHud.getLogger().warn(e);
                    failed.setTrue();
                }
            }).withComponentHandler((n, c) -> {
                try {
                    IComponentProvider prov = providers.get(n.toLowerCase());
                    if (prov != null)
                        components.add(prov.create(c));
                    else
                        RPHud.warn("Invalid component type: {}", n);
                } catch (Exception e) {
                    RPHud.warn("Errored on component statement!");
                    RPHud.warn(e);
                    failed.setTrue();
                }
            }).parse(cfgFile);
        } catch (Exception e) {
            RPHud.error("General parsing failure!");
            RPHud.error(e);
            failed.setTrue();
        }
        if (failed.booleanValue())
            clearCache();
    }

    public boolean shouldCancel(RenderGameOverlayEvent.ElementType type) {
        return toCancel.contains(type);
    }

    public void doRender(boolean afterHud) {
        if (!failed) {
            ScaledResolution res = new ScaledResolution(mc);
            eval.context(new ExprContext(mc.ingameGUI, mc.thePlayer, res));
            components.stream().filter(c -> c.renderUnderHud() != afterHud).forEach(c -> {
                try {
                    if (c.shouldRender(mc, res, eval))
                        c.render(mc, res, eval);
                } catch (Exception e) {
                    RPHud.error("Encountered {} while rendering component {}!",
                            e.getClass().getSimpleName(),
                            c.getClass().getName());
                    RPHud.error(e);
                    failed = true;
                }
            });
            eval.exitContext();
        }
    }

}
