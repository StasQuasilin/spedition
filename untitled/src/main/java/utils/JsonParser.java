package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {
    private final JSONParser parser = new JSONParser();

    public JSONObject parse(String message) {
        try {
            return (JSONObject)parser.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
