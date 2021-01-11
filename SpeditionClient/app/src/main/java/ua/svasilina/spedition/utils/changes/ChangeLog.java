package ua.svasilina.spedition.utils.changes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import ua.svasilina.spedition.entity.CurrentLocation;
import ua.svasilina.spedition.entity.JsonAble;

import static ua.svasilina.spedition.constants.Keys.CHANGES;
import static ua.svasilina.spedition.constants.Keys.LOCATION;
import static ua.svasilina.spedition.constants.Keys.TIME;

public class ChangeLog implements JsonAble {
    private final Calendar time;
    private CurrentLocation location;
    private ArrayList<Change> changes;

    public ChangeLog(ArrayList<Change> changes) {
        this.time = Calendar.getInstance();
        this.changes = changes;
    }

    public CurrentLocation getLocation() {
        return location;
    }

    public void setLocation(CurrentLocation location) {
        this.location = location;
    }

    public Calendar getTime() {
        return time;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(TIME, time.getTimeInMillis());
        jsonObject.put(LOCATION, location.toJson());
        jsonObject.put(CHANGES, changes());
        return jsonObject;
    }

    private JSONArray changes() {
        final JSONArray array = new JSONArray();
        for (Change change : changes){
            array.add(change.toJson());
        }
        return array;
    }
}
