package info.breezes.itebooks.app.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DownloadReceiver extends BroadcastReceiver {
    public DownloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DownloadReceiver",intent.getAction());
    }
}
