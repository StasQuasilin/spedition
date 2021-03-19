package ua.svasilina.spedition.utils.location;

import android.content.Context;
import android.location.Location;

import org.json.JSONObject;

import java.util.HashMap;

import ua.svasilina.spedition.constants.ApiLinks;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.utils.network.DataSender;

public class LocationRegistrator implements OnLocationGet{

    private final DataSender sender;
    private final HashMap<String, Object> dataMap = new HashMap<>();
    private final String uuid;

    public LocationRegistrator(Context context, String uuid){
        sender = new DataSender(context);
        this.uuid = uuid;
    }

    Location lastLocation = null;

    @Override
    public void locate(Location location) {
        if (lastLocation != null){
            System.out.println(lastLocation.equals(location));
            lastLocation = location;
        }
        dataMap.clear();
        dataMap.put(Keys.TIME, location.getTime());
        dataMap.put(Keys.REPORT, uuid);
        dataMap.put(Keys.LATITUDE, location.getLatitude());
        dataMap.put(Keys.LONGITUDE, location.getLongitude());
        dataMap.put(Keys.SPEED, location.getSpeed());
        sender.send(ApiLinks.SAVE_LOCATION, new JSONObject(dataMap), System.out::println, Throwable::printStackTrace);
    }
}
