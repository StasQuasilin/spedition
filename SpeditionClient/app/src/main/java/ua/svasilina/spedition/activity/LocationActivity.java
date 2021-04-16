package ua.svasilina.spedition.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.utils.location.LocationUtil;

public class LocationActivity extends AppCompatActivity {

    private LocationUtil locationUtil;
    private Timer locationTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_view);
        final TextView locationView = findViewById(R.id.locationView);

        final Context context = getApplicationContext();
        locationUtil = new LocationUtil(context);
        final TimerTask timerTask = new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                locationUtil.getLastLocation(location -> {
                    final double longitude = location.getLongitude();
                    final double latitude = location.getLatitude();
                    locationView.setText(latitude + " x " + longitude);
                });
            }
        };
        locationTimer = new Timer();
        int DELAY = 10 * 1000;
        locationTimer.schedule(timerTask, 0, DELAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationTimer != null) {
            locationTimer.cancel();
        }
    }
}
