package ua.svasilina.spedition.utils.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import ua.svasilina.spedition.constants.ApiLinks;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.utils.JsonParser;
import ua.svasilina.spedition.utils.StorageUtil;
import ua.svasilina.spedition.utils.db.JsonObject;
import ua.svasilina.spedition.utils.network.DataSender;

public class LocationRegistrator implements OnLocationGet{

    private static final float ACCURACY = 0.0000001f;
    private final DataSender sender;
    private final HashMap<String, Object> dataMap = new HashMap<>();
    private final JSONObject data = new JSONObject();
    private final String uuid;
    private final StorageUtil storageUtil;
    private static final String LAST_LOCATION = "localocaloca:)";
    private static final String LOCATION_HISTORY = "LaVidaLoca";
    Handler handler;
    Message message;
    Bundle bundle;

    public LocationRegistrator(Context context, String uuid){
        sender = new DataSender(context);
        storageUtil = new StorageUtil(context);
        this.uuid = uuid;
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message message) {
                final Bundle data = message.getData();
                String msg = "";
                if(data.containsKey(Keys.CODE)) {
                    final double la = data.getDouble(Keys.LATITUDE);
                    final double lo = data.getDouble(Keys.LONGITUDE);
                    msg = la + " x " + lo;
                } else if (data.containsKey(Keys.MESSAGE)){
                    msg = data.getString(Keys.MESSAGE);
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        };
        message = new Message();
    }

    @Override
    public void locate(Location location) {
        if (notEquals(location)){
            double la = location.getLatitude();
            double lo = location.getLongitude();

            dataMap.put(Keys.TIME, location.getTime());
            dataMap.put(Keys.REPORT, uuid);
            dataMap.put(Keys.LATITUDE, la);
            dataMap.put(Keys.LONGITUDE, lo);
            dataMap.put(Keys.SPEED, location.getSpeed());

            final JSONArray array = readLocationHistory();
            array.put(new JSONObject(dataMap));
            dataMap.put(Keys.DATA, array);
            sender.send(ApiLinks.SAVE_LOCATION, new JSONObject(dataMap),
                    success -> {
                        storageUtil.saveData(LOCATION_HISTORY, Keys.EMPTY);
                        final Bundle bundle = new Bundle();
                        bundle.putDouble(Keys.LONGITUDE, lo);
                        bundle.putDouble(Keys.LATITUDE, la);
                        bundle.putInt(Keys.CODE, 200);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    },
                    error -> {
                        storageUtil.saveData(LOCATION_HISTORY, array.toString());
                        final Bundle bundle = new Bundle();
                        bundle.putString(Keys.MESSAGE, error.getMessage());
                        message.setData(bundle);
                        handler.sendMessage(message);
                        }
            );

            saveLocation(lo, la);
        }
    }

    private JSONArray readLocationHistory() {
        JSONArray array = new JSONArray();
        final String data = storageUtil.readFile(LOCATION_HISTORY);
        if(data != null){
            final org.json.simple.JSONArray dataArray = parser.parseArray(data);
            if(dataArray != null) {
                for (Object o : dataArray) {
                    array.put(parser.parse(String.valueOf(o)));
                }
            }
        }
        return array;
    }

    private void saveLocation(double lo, double la) {
        final org.json.simple.JSONObject jsonObject = new org.json.simple.JSONObject();
        jsonObject.put(Keys.LONGITUDE, lo);
        jsonObject.put(Keys.LATITUDE, la);
        storageUtil.saveData(LAST_LOCATION, jsonObject.toJSONString());
    }
    private final JsonParser parser = new JsonParser();
    private boolean notEquals(Location location) {

        final String data = storageUtil.readFile(LAST_LOCATION);
        if(data != null) {
            final JsonObject parsed = parser.parse(data);

            final double lo = parsed.getDouble(Keys.LONGITUDE);
            final double la = parsed.getDouble(Keys.LATITUDE);
            System.out.println(la + " x " + lo + " | " + location.getLatitude() + " x " + location.getLongitude());
            return Math.abs(lo - location.getLongitude()) > ACCURACY || Math.abs(la - location.getLatitude()) > ACCURACY;
        }

        return true;
    }

    public void stop() {
        storageUtil.remove(LAST_LOCATION);
        storageUtil.remove(LOCATION_HISTORY);
    }
}
