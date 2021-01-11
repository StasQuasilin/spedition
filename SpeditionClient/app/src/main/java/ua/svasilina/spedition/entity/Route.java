package ua.svasilina.spedition.entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import static ua.svasilina.spedition.constants.Keys.EMPTY;
import static ua.svasilina.spedition.constants.Keys.ROUTE;

public class Route implements JsonAble {

    private ArrayList<String> points = new ArrayList<>();
    public static final String ARROW = " ➞ ";

    public ArrayList<String> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<String> points) {
        this.points = points;
    }

    public String getValue(){
        if (points.size() > 0){
            StringBuilder builder = new StringBuilder();
            if (points.size() > 2){
                builder.append("...").append(ARROW);
                builder.append(points.get(points.size() - 2));
                builder.append(ARROW);
                builder.append(points.get(points.size() - 1));
            } else {
                for (int i = 0; i < points.size(); i++){
                    builder.append(points.get(i));
                    if (i < points.size() - 1){
                        builder.append(ARROW);
                    }
                }
            }

            return builder.toString().toUpperCase();
        }
        return EMPTY;
    }

    public void addPoint(String item) {
        points.add(item);
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(ROUTE, route());
        return jsonObject;
    }

    private JSONArray route() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < points.size(); i++){
            array.add(points.get(i));
        }
        return array;
    }

    public void clear() {
        points.clear();
    }

    public boolean isEmpty() {
        return points.size() == 0;
    }
}
