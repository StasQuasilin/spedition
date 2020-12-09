package ua.svasilina.spedition.utils.changes;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ua.svasilina.spedition.entity.CurrentLocation;
import ua.svasilina.spedition.entity.OldReport;
import ua.svasilina.spedition.services.LocationService;
import ua.svasilina.spedition.utils.NetworkUtil;
import ua.svasilina.spedition.utils.StorageUtil;

import static ua.svasilina.spedition.constants.Keys.EMPTY;

public class ChangeUtil {

    private final LocationService locationService;
    private final StorageUtil storageUtil;
    private NetworkUtil networkUtil;

    public ChangeUtil(Context context) {
        locationService = new LocationService(context);
        storageUtil = new StorageUtil(context);
        networkUtil = new NetworkUtil();
    }

    private final ArrayList<String> fieldList = new ArrayList<>();

    public ChangeLog compare(OldReport oldObject, OldReport newObject){
        HashMap<String, Object> oldValues = null;
        if (oldObject != null) {
            oldValues = oldObject.getValues(EMPTY);
            addFields(oldValues.keySet());
        }
        HashMap<String, Object> newValues = null;
        if (newObject != null) {
            newValues = newObject.getValues(EMPTY);
            addFields(newValues.keySet());
        }
        final ArrayList<Change> changes = new ArrayList<>();
        for (String field : fieldList){
            Object oldValue = null;
            if (oldValues != null && oldValues.containsKey(field)){
                oldValue = oldValues.get(field);
            }
            Object newValue = null;
            if (newValues != null && newValues.containsKey(field)){
                newValue = newValues.get(field);
            }
            if (oldValue != newValue){
                changes.add(new Change(field, oldValue, newValue));
            }
        }
        if (changes.size() > 0){
            ChangeLog changeLog = new ChangeLog(changes);
            changeLog.setLocation(new CurrentLocation(locationService.getLocation()));
            return changeLog;
        }
        return null;
    }

    void addFields(Set<String> fields){
        for (String field : fields){
            if (!this.fieldList.contains(field)){
                this.fieldList.add(field);
            }
        }
    }

}
