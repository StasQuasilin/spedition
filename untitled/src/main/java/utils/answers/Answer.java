package utils.answers;

import constants.Keys;
import entity.JsonAble;
import org.json.simple.JSONObject;

import java.util.HashMap;

public abstract class Answer extends JsonAble {

    public abstract String getKey();

    private final HashMap<String, Object> params = new HashMap<>();

    public void  addParam(String key, Object object){
        params.put(key, object);
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(Keys.KEY, getKey());
        jsonObject.putAll(params);
        return jsonObject;
    }
}
