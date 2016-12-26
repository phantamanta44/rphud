package io.github.phantamanta44.rphud.hud;

import io.github.phantamanta44.rphud.util.DeserializingMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HudParser {

    private static final String VALID_VAR_NAMES = "[\\w_]+";

    private Consumer<String> onCancel;
    private BiConsumer<String, String> onDefinition;
    private BiConsumer<String, DeserializingMap> onComponent;

    public HudParser withCancelHandler(Consumer<String> handler) {
        this.onCancel = handler;
        return this;
    }

    public HudParser withDefinitionHandler(BiConsumer<String, String> handler) {
        this.onDefinition = handler;
        return this;
    }

    public HudParser withComponentHandler(BiConsumer<String, DeserializingMap> handler) {
        this.onComponent = handler;
        return this;
    }

    public void parse(List<String> cfgFile) {
        BiConsumer<String, HudParser> scope = null;
        for (String line : cfgFile) {
            if (line.isEmpty())
                continue;
            line = line.trim();
            if (line.startsWith("-") && line.endsWith("-")) {
                line = line.substring(1, line.length() - 1).trim();
                scope = getScope(line.toLowerCase());
            } else {
                if (scope == null)
                    throw new IllegalArgumentException("Encountered line before scope definition: " + line);
                scope.accept(line, this);
            }
        }
    }

    private BiConsumer<String, HudParser> getScope(String name) {
        switch (name.toLowerCase()) {
            case "cancels":
                return new CancelScope();
            case "definitions":
                return new DefinitionScope();
            case "components":
                return new ComponentScope();
        }
        throw new IllegalArgumentException("No such scope: " + name);
    }

    private static class CancelScope implements BiConsumer<String, HudParser> {

        @Override
        public void accept(String line, HudParser parser) {
            parser.onCancel.accept(line);
        }

    }

    private static class DefinitionScope implements BiConsumer<String, HudParser> {

        @Override
        public void accept(String line, HudParser parser) {
            int eq = line.indexOf("=");
            if (eq == -1)
                throw new IllegalArgumentException("Expected an equals in declaration statement: " + line);
            String key = line.substring(0, eq).trim();
            if (!key.matches(VALID_VAR_NAMES))
                throw new IllegalArgumentException("Illegal declared name: " + key);
            parser.onDefinition.accept(key, line.substring(eq + 1).trim());
        }

    }

    private static class ComponentScope implements BiConsumer<String, HudParser> {

        private String componentName = null;
        private Map<String, String> props = null;

        @Override
        public void accept(String line, HudParser parser) {
            if (componentName == null) {
                componentName = line;
                props = new HashMap<>();
            } else if (line.equalsIgnoreCase("end")) {
                parser.onComponent.accept(componentName, new DeserializingMap(props));
                componentName = null;
                props = null;
            } else {
                int eq = line.indexOf("=");
                if (eq == -1)
                    throw new IllegalArgumentException("Expected an equals in property statement: " + line);
                props.put(line.substring(0, eq).trim(), line.substring(eq + 1).trim());
            }
        }

    }

}
