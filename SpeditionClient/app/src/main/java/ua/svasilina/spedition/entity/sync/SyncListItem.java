package ua.svasilina.spedition.entity.sync;

import org.json.simple.JSONObject;

import java.util.Calendar;

import ua.svasilina.spedition.entity.JsonAble;

import static ua.svasilina.spedition.constants.Keys.REPORT;
import static ua.svasilina.spedition.constants.Keys.TIME;

public class SyncListItem implements JsonAble {
    private final String report;
    private Calendar syncTime;

    public SyncListItem(String report) {
        this.report = report;
    }

    public String getReport() {
        return report;
    }

    public Calendar getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Calendar syncTime) {
        this.syncTime = syncTime;
    }

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(REPORT, report);
        if (syncTime != null) {
            jsonObject.put(TIME, syncTime.getTimeInMillis());
        }
        return jsonObject;
    }
}
