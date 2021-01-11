package ua.svasilina.spedition.utils.changes;

import java.util.HashMap;

public interface IChanged {
    HashMap<String, Object> getValues(String key);
}

