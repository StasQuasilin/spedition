package ua.svasilina.spedition.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import ua.svasilina.spedition.R;

public class OnConnectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager systemService = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = systemService.getActiveNetworkInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback();
        }
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Toast.makeText(context, R.string.wifi_connected, Toast.LENGTH_SHORT).show();
        } else if (networkInfo != null){
            Toast.makeText(context, R.string.any_connected, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.lost_connection, Toast.LENGTH_SHORT).show();
        }
    }
}
