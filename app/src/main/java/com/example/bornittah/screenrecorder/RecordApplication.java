package com.example.bornittah.screenrecorder;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Bornittah on 11/17/2017.
 */

public class RecordApplication extends Application {

    public static RecordApplication application;

    public void attachBaseContext(Context base){
        super.attachBaseContext(base);
        application = this;
    }
    public void onCreate(){
        super.onCreate();
        //start service
        startService(new Intent(this, RecordServices.class));

    }
    public static RecordApplication getInstance(){
        return application;
    }
}
