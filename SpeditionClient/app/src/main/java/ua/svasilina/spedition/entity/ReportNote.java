package ua.svasilina.spedition.entity;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Calendar;

import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.NOTE;
import static ua.svasilina.spedition.constants.Keys.TIME;

public class ReportNote implements Serializable, JsonAble {
    private String uuid;
    private Calendar time;
    private String note;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Calendar getTime() {
        return time;
    }
    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(ID, uuid);
        jsonObject.put(TIME, time.getTimeInMillis());
        jsonObject.put(NOTE, note);
        return jsonObject;
    }
}
