package entity;

import org.json.simple.JSONObject;

public abstract class JsonAble {
    public JSONObject getJsonObject(){
        return new JSONObject();
    }

    public JSONObject toSimpleJson(){
        return toJson();
    }

    public abstract JSONObject toJson();
}
