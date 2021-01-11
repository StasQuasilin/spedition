package ua.svasilina.spedition.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {
    private final JSONParser parser;

    public JsonParser() {
        parser = new JSONParser();
    }

    public JSONObject parse(String data){
        try {
            return (JSONObject) parser.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
