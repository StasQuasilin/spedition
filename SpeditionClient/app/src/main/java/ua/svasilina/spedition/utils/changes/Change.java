package ua.svasilina.spedition.utils.changes;

import org.json.simple.JSONObject;

import ua.svasilina.spedition.entity.JsonAble;

import static ua.svasilina.spedition.constants.Keys.FIELD;
import static ua.svasilina.spedition.constants.Keys.NEW;
import static ua.svasilina.spedition.constants.Keys.OLD;

public class Change implements JsonAble {
    private final String field;
    private final Object oldValue;
    private final Object newValue;

    public Change(String field, Object oldValue, Object newValue) {
        this.field = field;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELD, field);
        jsonObject.put(OLD, oldValue.toString());
        jsonObject.put(NEW, newValue.toString());
        return jsonObject;
    }
}
