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
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.activity.ReportEdit;
import ua.svasilina.spedition.constants.Keys;

import static ua.svasilina.spedition.constants.Keys.ID;

public class ActiveReportService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final String CHANNEL_NAME = "Foreground Service Channel";
    public static final int NOTIFICATION_ID = 100200300;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        final String route = intent.getStringExtra(Keys.ROUTE);
        final Context context = getApplicationContext();
        Intent editIntent = new Intent(context, ReportEdit.class);
        final long id = intent.getLongExtra(ID, -1);
        editIntent.putExtra(ID, id);

        editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, editIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(route)
                .setContentText(getResources().getString(R.string.press_for_open))
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
        Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //todo location service - fix location
                Log.i("Background", "is alive");
            }
        };
//        timer.schedule(timerTask, 0, 1000);

        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}
