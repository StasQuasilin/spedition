package ua.svasilina.spedition.entity.sync;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ua.svasilina.spedition.entity.JsonAble;

import static ua.svasilina.spedition.constants.Keys.FIELDS;

public class SyncList implements JsonAble {

    private final HashMap<String, SyncListItem> fields = new HashMap<>();

    @Override
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put(FIELDS, fields());
        return jsonObject;
    }

    private JSONArray fields() {
        final JSONArray array = new JSONArray();
        for (Map.Entry<String, SyncListItem> entry : fields.entrySet()){
            array.add(entry.getValue().toJson());
        }
        return array;
    }

    public void addField(SyncListItem item) {
        fields.put(item.getReport(), item);
    }

    public void remove(String report){
        fields.remove(report);
    }

    public void clearTime(String report) {
        getItem(report).setSyncTime(null);
    }

    public Collection<SyncListItem> getFields() {
        return fields.values();
    }

    public void setSyncTime(String report) {
        final SyncListItem item = getItem(report);
        final Calendar syncTime = item.getSyncTime();
        final Calendar now = Calendar.getInstance();
        if (syncTime == null || syncTime.before(now)){
            item.setSyncTime(now);
        }
    }

    private SyncListItem getItem(String report){
        SyncListItem syncListItem = fields.get(report);
        if (syncListItem == null){
            syncListItem = new SyncListItem(report);
            addField(syncListItem);
        }
        return syncListItem;
    }
}
