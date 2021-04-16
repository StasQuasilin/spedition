package ua.svasilina.spedition.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ua.svasilina.spedition.utils.db.JsonObject;

public class JsonParser {
    private final JSONParser parser;

    public JsonParser() {
        parser = new JSONParser();
    }

    public JsonObject parse(String data){
        try {
            JSONObject json = (JSONObject) parser.parse(data);
            return new JsonObject(json);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray parseArray(String data){
        try {
            return (JSONArray)parser.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
