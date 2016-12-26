package io.github.phantamanta44.rphud.util;

import java.util.Map;
import java.util.NoSuchElementException;

public class DeserializingMap {
    
    private final Map<String, String> backing;
    
    public DeserializingMap(Map<String, String> backing) {
        this.backing = backing;
    }
    
    public boolean has(String key) {
        return backing.containsKey(key);
    }
    
    public String get(String key) {
        return backing.get(key);
    }
    
    public String getString(String key) {
        keyCheck(key);
        return get(key);
    }
    
    public int getInt(String key) {
        keyCheck(key); 
        try {
            return Integer.parseInt(get(key));
        } catch (NumberFormatException e) {
            throw new ClassCastException("Could not be deserialized as int!");
        }
    }

    public float getFloat(String key) {
        keyCheck(key);
        try {
            return Float.parseFloat(get(key));
        } catch (NumberFormatException e) {
            throw new ClassCastException("Could not be deserialized as float!");
        }
    }

    public double getDouble(String key) {
        keyCheck(key);
        try {
            return Double.parseDouble(get(key));
        } catch (NumberFormatException e) {
            throw new ClassCastException("Could not be deserialized as double!");
        }
    }

    public boolean getBool(String key) {
        keyCheck(key);
        switch (key.toLowerCase()) {
            case "true":
            case "yes":
            case "on":
            case "enabled":
                return true;
            case "false":
            case "no":
            case "off":
            case "disabled":
                return false;
        }
        throw new ClassCastException("Could not be deserialized as boolean!");
    }
    
    private void keyCheck(String key) {
        if (!has(key))
            throw new NoSuchElementException("No such key " + key + "!");
    }
    
}
