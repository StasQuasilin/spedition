package ua.svasilina.spedition.utils.sync;

import android.content.Context;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Calendar;

import ua.svasilina.spedition.entity.sync.SyncList;
import ua.svasilina.spedition.entity.sync.SyncListItem;
import ua.svasilina.spedition.utils.JsonParser;
import ua.svasilina.spedition.utils.StorageUtil;

import static ua.svasilina.spedition.constants.Keys.FIELDS;
import static ua.svasilina.spedition.constants.Keys.REPORT;
import static ua.svasilina.spedition.constants.Keys.TIME;

public class SyncListUtil {

    public static final String TAG = "Sync List Util";
    private static final String SYNC_LIST_FILE_NAME = "SyncList";
    private static final String REMOVE_LIST = "RemoveList";
    private final StorageUtil storageUtil;
    private final JsonParser parser;

    public SyncListUtil(Context context) {
        storageUtil = new StorageUtil(context);
        parser = new JsonParser();
    }

    public SyncList readSyncList(){
        return readList(SYNC_LIST_FILE_NAME);
    }

    public SyncList readRemoveList(){
        return readList(REMOVE_LIST);
    }

    private SyncList readList(String fileName){
        final String data = storageUtil.readFile(fileName);
        SyncList list = new SyncList();
        if (data != null){
            final JSONObject parse = parser.parse(data);
            if (parse != null){
                parseList(list, parse);
            }
        }

        return list;
    }

    public void saveSyncList(SyncList list){
        final JSONObject jsonObject = list.toJson();
        storageUtil.saveData(SYNC_LIST_FILE_NAME, jsonObject.toJSONString());
    }

    private void parseList(SyncList list, JSONObject data) {
        final Object f = data.get(FIELDS);
        if (f != null){
            for (Object o : (JSONArray)f){
                JSONObject field = (JSONObject) o;
                final String key = String.valueOf(field.get(REPORT));

                SyncListItem item = new SyncListItem(key);
                if (field.containsKey(TIME)){
                    final Calendar instance = Calendar.getInstance();
                    instance.setTimeInMillis(Long.parseLong(String.valueOf(field.get(TIME))));
                    item.setSyncTime(instance);
                }

                list.addField(item);
            }
        }
    }

    public void resetSyncTime(String report) {
        Log.i(TAG, "Reset sync time for '" + report + "'");
        final SyncList list = readSyncList();
        list.clearTime(report);
        saveSyncList(list);
    }

    public void setSyncTime(String report) {
        Log.i(TAG, "Set sync time for '" + report + "'");
        final SyncList list = readSyncList();
        list.setSyncTime(report);
        saveSyncList(list);
    }

    public void addRemove(String uuid) {
        final SyncList removeList = readRemoveList();
        removeList.clearTime(uuid);
        System.out.println("------------->");
        for (SyncListItem i : removeList.getFields()){
            System.out.println(i.getReport());
        }
        System.out.println("<-------------");
        saveSyncList(removeList);
    }

    public void forgotAbout(String report) {
        final SyncList removeList = readRemoveList();
        removeList.clearTime(report);
        saveSyncList(removeList);
        Log.i(TAG, "Forgot about " + report);
        final SyncList syncList = readSyncList();
        syncList.remove(report);
        saveSyncList(syncList);
    }
}
