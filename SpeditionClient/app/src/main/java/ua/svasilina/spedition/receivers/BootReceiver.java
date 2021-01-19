package ua.svasilina.spedition.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ua.svasilina.spedition.utils.background.BackgroundWorkerUtil;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action != null){
            if (action.equals(Intent.ACTION_BOOT_COMPLETED)){
                BackgroundWorkerUtil.getInstance().runWorker(context);
            }
        }
    }
}
