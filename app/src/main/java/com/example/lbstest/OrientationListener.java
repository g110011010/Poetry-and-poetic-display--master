package com.example.lbstest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BeiYun on 2017/8/3.
 */

public class OrientationListener implements SensorEventListener {
    private Context context;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float lastX ;
    private OnOrientationListener onOrientationListener ;
    public OrientationListener(Context context)
    {
        this.context = context;
    }
    public void start() {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null)
        {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if (sensor != null)
        {
            sensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
    public void stop()
    {
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            float x = event.values[SensorManager.DATA_X];
            if( Math.abs(x- lastX) > 1.0 )
            {
                onOrientationListener.onOrientationChanged(x);
            }
            lastX = x ;
        }
    }
    public void setOnOrientationListener(OnOrientationListener onOrientationListener)
    {
        this.onOrientationListener = onOrientationListener ;
    }
    public interface OnOrientationListener
    {
        void onOrientationChanged(float x);
    }
}