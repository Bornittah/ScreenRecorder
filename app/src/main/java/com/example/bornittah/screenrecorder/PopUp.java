package com.example.bornittah.screenrecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Bornittah on 11/18/2017.
 */

public class PopUp extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Recording ended", Toast.LENGTH_LONG).show();
      //  Toast.makeText(context,"Recording stop",Toast.LENGTH_LONG).show();
    }
}
