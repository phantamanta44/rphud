package io.github.phantamanta44.rphud.hud;

import io.github.phantamanta44.rphud.RPHConst;
import io.github.phantamanta44.rphud.util.DeserializingMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class HudParser {

    private static final String VALID_VAR_NAMES = "[\\w_]+";

    private final Minecraft mc;
    private Consumer<String> onCancel;
    private BiConsumer<String, String> onDefinition;
    private BiConsumer<String, DeserializingMap> onComponent;

    public HudParser() {
        this.mc = Minecraft.getMinecraft();
    }

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
        BiFunction<String, HudParser, List<String>> scope = null;
        for (int i = 0; i < cfgFile.size(); i++) {
            String line = cfgFile.get(i).trim();
            if (line.isEmpty() || line.startsWith("#"))
                continue;
            if (line.startsWith("-") && line.endsWith("-")) {
                line = line.substring(1, line.length() - 1).trim();
                scope = getScope(line.toLowerCase());
            } else {
                if (scope == null)
                    throw new IllegalArgumentException("Encountered line before scope definition: " + line);
                List<String> result = scope.apply(line, this);
                if (result != null)
                    cfgFile.addAll(i, result);
            }
        }
    }

    private BiFunction<String, HudParser, List<String>> getScope(String name) {
        switch (name.toLowerCase()) {
            case "cancels":
                return new CancelScope();
            case "definitions":
                return new DefinitionScope();
            case "components":
                return new ComponentScope();
            case "imports":
                return new ImportScope();
        }
        throw new IllegalArgumentException("No such scope: " + name);
    }

    private static class CancelScope implements BiFunction<String, HudParser, List<String>> {

        @Override
        public List<String> apply(String line, HudParser parser) {
            parser.onCancel.accept(line);
            return null;
        }

    }

    private static class DefinitionScope implements BiFunction<String, HudParser, List<String>> {

        @Override
        public List<String> apply(String line, HudParser parser) {
            int eq = line.indexOf("=");
            if (eq == -1)
                throw new IllegalArgumentException("Expected an equals in declaration statement: " + line);
            String key = line.substring(0, eq).trim();
            if (!key.matches(VALID_VAR_NAMES))
                throw new IllegalArgumentException("Illegal declared name: " + key);
            parser.onDefinition.accept(key, line.substring(eq + 1).trim());
            return null;
        }

    }

    private static class ComponentScope implements BiFunction<String, HudParser, List<String>> {

        private String componentName = null;
        private Map<String, String> props = null;

        @Override
        public List<String> apply(String line, HudParser parser) {
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
            return null;
        }

    }

    private static class ImportScope implements BiFunction<String, HudParser, List<String>> {

        @Override
        public List<String> apply(String line, HudParser parser) {
            try {
                ResourceLocation loc = new ResourceLocation(RPHConst.MOD_ID, line);
                return IOUtils.readLines(parser.mc.getResourceManager().getResource(loc).getInputStream());
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("No such importable file: " + line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
