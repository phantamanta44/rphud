package io.github.phantamanta44.rphud.hud.component;

import io.github.phantamanta44.rphud.util.DeserializingMap;

public abstract class AbstractComponent implements IComponent {

    protected final boolean underHud;

    public AbstractComponent(DeserializingMap cfg) {
        this.underHud = cfg.has("underhud") && cfg.getBool("underhud");
    }

    @Override
    public boolean renderUnderHud() {
        return underHud;
    }

}
