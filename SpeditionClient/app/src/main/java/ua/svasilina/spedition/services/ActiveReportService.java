package ua.svasilina.spedition.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.activity.ReportEdit;
import ua.svasilina.spedition.activity.Reports;
import ua.svasilina.spedition.constants.Keys;
import ua.svasilina.spedition.utils.background.BackgroundWorkerUtil;
import ua.svasilina.spedition.utils.location.LocationRegistrator;
import ua.svasilina.spedition.utils.location.LocationUtil;

import static ua.svasilina.spedition.constants.Keys.ID;
import static ua.svasilina.spedition.constants.Keys.UUID;

public class ActiveReportService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String CHANNEL_NAME = "Foreground Service Channel";
    public static final int NOTIFICATION_ID = 100200300;
    private static final int LOCATION_PERIOD = 200 * 60 * 1000;
    private LocationUtil locationUtil;
    private Timer locationTimer;
    private LocationRegistrator registrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        String route = null;
        String uuid = null;
        if (intent != null){
            route = intent.getStringExtra(Keys.ROUTE);
            uuid = intent.getStringExtra(UUID);
        }

        final Context context = getApplicationContext();
        locationUtil = new LocationUtil(context);
        BackgroundWorkerUtil.getInstance().runWorker(context);

        Intent editIntent = new Intent();
        if (uuid != null){
            editIntent.setClass(context,ReportEdit.class);
            editIntent.putExtra(ID, uuid);
        }else {
            editIntent.setClass(context, Reports.class);
        }

        editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(route)
                .setContentText(getResources().getString(R.string.press_for_open))
                .setSmallIcon(R.drawable.ic_truck_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        startForeground(NOTIFICATION_ID, notification);
        locationTimer = new Timer();
        registrator = new LocationRegistrator(context, uuid);
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                locationUtil.getLastLocation(registrator);
            }
        };
        locationTimer.schedule(timerTask, 0, LOCATION_PERIOD);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationTimer != null){
            locationTimer.cancel();
            registrator.stop();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
