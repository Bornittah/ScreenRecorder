package com.example.bornittah.screenrecorder;

import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Bornittah on 11/17/2017.
 */

public class RecordServices extends Service {
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;
    private boolean running;
    private int width=720;
    private int height=1080;
    private int dpi;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();

    }
    public int onStartCommand(Intent intent, int flags, int startId){

        return START_STICKY;
    }
    public void onCreate(){
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running=false;
        mediaRecorder=new MediaRecorder();
    }
    public void onDestroy() {
       super.onDestroy();
    }

    public void setMediaProjection(MediaProjection mproject){
        mediaProjection = mproject;
    }
    public boolean isRunning(){
      return running;
    }
    public void setConfig(int width, int height, int dpi){
        this.width=width;
        this.height=height;
        this.dpi=dpi;
    }
    public boolean startRecord(){
    if (mediaProjection == null || running){
        return false;
    }
        initRecorder();
        createVitualDisplay();
        mediaRecorder.start();
        running=true;
        return  true;
        }

    public boolean stopRecord(){
        if(!running) {
        return false;
        }
          running = false;
          mediaRecorder.stop();
            mediaRecorder.reset();
            virtualDisplay.release();
            mediaProjection.stop();

            return true;
        }


    public void createVitualDisplay() {
        virtualDisplay=mediaProjection.createVirtualDisplay("mainScreen", width, height, dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(),null, null);

    }

    public void initRecorder() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(getsaveDirectory() + System.currentTimeMillis() + ".mp4");
        mediaRecorder.setVideoSize(width, height);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        mediaRecorder.setVideoFrameRate(50);

        try{
            mediaRecorder.prepare();
        }catch(IOException e){
           e.getStackTrace();
        }
    }

    public String getsaveDirectory() {
    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecorder" + "/";

        File file = new File(rootDir);
        if (!file.exists()) {
            if (file.mkdir()) {
                return null;
            }

        }

        Toast.makeText(getApplicationContext(), rootDir, Toast.LENGTH_SHORT).show();
        return rootDir;
    }
        else{
            return null;
        }
    }



    public class RecordBinder extends Binder {
    public RecordServices getRecorderService() {
        return RecordServices.this;
       }
    }
}
