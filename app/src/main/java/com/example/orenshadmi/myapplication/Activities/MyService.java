package com.example.orenshadmi.myapplication.Activities;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;


/**
 * Created by Maor on 02/01/2018.
 */
public class MyService extends Service implements SensorEventListener {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();
    private float X, Y ,Z;

    private static final String TAG = "MyService";
    private SensorManager sensorManager;
    Sensor accelemetor;

    @SuppressLint("ServiceCast")
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelemetor =sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MyService.this,accelemetor,SensorManager.SENSOR_DELAY_NORMAL);


    }
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        MyService getService() {
            // Return this instance of MyService so clients can call public methods
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    /** method for clients */
    public int getRandomNumber() {
        return mGenerator.nextInt(100);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
          Log.d(TAG,"onSensorChanged X:" +event.values[0] + "Y:" + event.values[1] + "Z:" + event.values[2]);
        X = event.values[0];
        Y = event.values[1];
        Z = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public float getXaccelemetor()
    {
        return X;
    }
    public float getYaccelemetor()
    {
        return Y;
    }
    public float getZaccelemetor()
    {
        return Z;
    }

}



