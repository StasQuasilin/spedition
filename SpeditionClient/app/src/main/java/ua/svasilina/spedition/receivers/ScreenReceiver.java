package ua.svasilina.spedition.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ua.svasilina.spedition.utils.background.BackgroundWorkerUtil;

public class ScreenReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            BackgroundWorkerUtil.getInstance().runWorker(context);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Toast.makeText(context, "Screen ON", Toast.LENGTH_SHORT).show();
        }
    }
}
