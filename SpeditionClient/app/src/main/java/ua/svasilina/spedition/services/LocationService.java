package ua.svasilina.spedition.services;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import ua.svasilina.spedition.R;

public class LocationService {

    final Context context;
    final LocationManager locationService;

    public LocationService(Context context) {
        this.context = context;
        locationService = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return locationService.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return locationService.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }

    public void checkGranted(final Activity activity){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.message_location_permission)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, 99);
                        }
                    })
                    .create()
                    .show();
        }
    }
}
