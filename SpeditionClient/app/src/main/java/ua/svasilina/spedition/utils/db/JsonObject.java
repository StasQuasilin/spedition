package ua.svasilina.spedition.utils.db;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonObject {

    private final JSONObject json;

    public JsonObject(JSONObject json) {
        this.json = json;
    }

    public boolean containsKey(String key) {
        return json.containsKey(key);
    }

    public long getLongOrDefault(String key, long def) {
        if (json.containsKey(key)){
            return getLong(key);
        }
        return def;
    }

    private long getLong(String key) {
        return Long.parseLong(getString(key));
    }
    public String getStringOrNull(String key){
        if (containsKey(key)){
            return getString(key);
        }
        return null;
    }
    public String getString(String key) {
        return String.valueOf(json.get(key));
    }

    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    public int[] getIntArray(String key) {
        if (json.containsKey(key)){
            JSONArray array = (JSONArray) json.get(key);

            if (array != null) {
                int[] ints = new int[array.size()];
                int i = 0;
                for (Object o: array){
                    ints[i] = (int) o;
                    i++;
                }
                return ints;
            }
        }
        return EMPTY_INT_ARRAY;
    }

    public String[] getStringArray(String key) {
        if (containsKey(key)){
            JSONArray array = (JSONArray) json.get(key);
            if (array != null) {
                String[] arr = new String[array.size()];
                int i = 0;
                for (Object o : arr){
                    arr[i++] = String.valueOf(o);
                }
                return arr;
            }
        }
        return EMPTY_STRING_ARRAY;
    }

    public JSONArray getJsonArray(String key) {
        if (containsKey(key)){
            return (JSONArray) json.get(key);
        }
        return null;
    }

    public JSONObject getJsonObject(String key) {
        if(containsKey(key)){
            return (JSONObject) json.get(key);
        }
        return null;
    }

    public JSONObject getOriginJson() {
        return json;
    }

    public double getDouble(String key) {
        return Double.parseDouble(getString(key));
    }
}
