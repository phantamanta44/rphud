package io.github.phantamanta44.rphud.hud.component;

import io.github.phantamanta44.rphud.util.DeserializingMap;

public interface IComponentProvider {

    String getName();

    IComponent create(DeserializingMap cfg);

}
