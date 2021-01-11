package ua.svasilina.spedition.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import ua.svasilina.spedition.R;
import ua.svasilina.spedition.utils.LoginUtil;

public class SyncService extends IntentService {

    LoginUtil loginUtil;

    public SyncService() {
        super("Sync Service");
        loginUtil = new LoginUtil(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        final String channelId = "0x18895";
        final String channelName = "Notifications";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);

            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.sync_dialog);
        final Notification notifications = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setCustomContentView(views)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
        startForeground(100500, notifications);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        long endTime = System.currentTimeMillis() + 10 * 1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service stopping", Toast.LENGTH_SHORT).show();
    }
}
