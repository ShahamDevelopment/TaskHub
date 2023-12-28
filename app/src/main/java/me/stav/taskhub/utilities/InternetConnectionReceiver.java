package me.stav.taskhub.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class InternetConnectionReceiver extends BroadcastReceiver {

    private InternetConnection internetConnection;
    public InternetConnectionReceiver() {
        internetConnection = new InternetConnection();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Internet broadcast
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            internetConnection.setConnected(true);
        } else {
            internetConnection.setConnected(false);
        }
    }

    public InternetConnection getInternetConnection() {
        return internetConnection;
    }
}