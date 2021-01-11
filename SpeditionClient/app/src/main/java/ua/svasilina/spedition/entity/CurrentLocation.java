package ua.svasilina.spedition.entity;

import android.location.Location;

import org.json.simple.JSONObject;

import static ua.svasilina.spedition.constants.Keys.LATITUDE;
import static ua.svasilina.spedition.constants.Keys.LONGITUDE;
import static ua.svasilina.spedition.constants.Keys.TIME;

public class CurrentLocation implements JsonAble {
    private double latitude;
    private double longitude;
    private long time;

    public CurrentLocation(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            time = location.getTime();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(LATITUDE, latitude);
        jsonObject.put(LONGITUDE , longitude);
        jsonObject.put(TIME, time);
        return jsonObject;
    }
}
