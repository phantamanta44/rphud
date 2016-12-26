package io.github.phantamanta44.rphud.hud.component;

import io.github.phantamanta44.rphud.util.DeserializingMap;

import java.util.function.Function;

public class SimpleComponentProvider implements IComponentProvider {

    private final String name;
    private final Function<DeserializingMap, IComponent> factory;

    public SimpleComponentProvider(String name, Function<DeserializingMap, IComponent> factory) {
        this.name = name;
        this.factory = factory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IComponent create(DeserializingMap cfg) {
        return factory.apply(cfg);
    }

}
