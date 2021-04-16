package utils;

import constants.Keys;
import entity.Expense;
import entity.ExpenseType;
import org.hibernate.jdbc.Expectation;
import org.json.simple.JSONObject;
import utils.hibernate.Hibernator;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Test {

    public static void main(String[] args) {

        String s = "{\"data\":[\"{\"latitude\":50.8618128,\"report\":\"08385a9f-c9b0-4293-9255-c9f0a1248ad0\",\"time\":1618572326928,\"speed\":0,\"longitude\":34.9480291}\",\"{\"latitude\":50.8618384,\"report\":\"08385a9f-c9b0-4293-9255-c9f0a1248ad0\",\"time\":1618572427358,\"speed\":0,\"longitude\":34.9475703}\",\"{\"latitude\":50.8618313,\"report\":\"08385a9f-c9b0-4293-9255-c9f0a1248ad0\",\"time\":1618572627809,\"speed\":0,\"longitude\":34.9474992}\",\"{\"latitude\":50.8618314,\"report\":\"08385a9f-c9b0-4293-9255-c9f0a1248ad0\",\"time\":1618572752617,\"speed\":0,\"longitude\":34.9475004}\",\"{\"latitude\":50.8618311,\"report\":\"1cc83ae9-c75c-4104-9848-de3705d336f8\",\"time\":1618574504314,\"speed\":0,\"longitude\":34.9474985}\",\"{\"latitude\":50.8618308,\"report\":\"1cc83ae9-c75c-4104-9848-de3705d336f8\",\"time\":1618574574814,\"speed\":0,\"longitude\":34.9474971}\",\"{\"latitude\":50.8618288,\"report\":\"1cc83ae9-c75c-4104-9848-de3705d336f8\",\"time\":1618574748322,\"speed\":0,\"longitude\":34.9474908}\",\"{\"latitude\":50.8618021,\"report\":\"1cc83ae9-c75c-4104-9848-de3705d336f8\",\"time\":1618574871681,\"speed\":0,\"longitude\":34.9475309}\",\"{\"latitude\":50.8617596,\"report\":\"1cc83ae9-c75c-4104-9848-de3705d336f8\",\"time\":1618575061166,\"speed\":0,\"longitude\":34.9477087}\",\"{\"latitude\":50.8618501,\"report\":\"1cc83ae9-c75c-4104-9848-de3705d336f8\",\"time\":1618575221537,\"speed\":0.0032379266,\"longitude\":34.9474959}\",{\"latitude\":50.8618384,\"report\":\"1cc83ae9-c75c-4104-9848-de3705d336f8\",\"time\":1618575749746,\"speed\":0,\"longitude\":34.9474826}],\"latitude\":50.8618384,\"report\":\"1cc83ae9-c75c-4104-9848-de3705d336f8\",\"time\":1618575749746,\"speed\":0,\"longitude\":34.9474826}";
        JsonParser parser = new JsonParser();
        final JSONObject parse = parser.parse(s);
        if(parse.containsKey(Keys.DATA)){
            System.out.println(parse.get(Keys.DATA));
        }
    }
}
