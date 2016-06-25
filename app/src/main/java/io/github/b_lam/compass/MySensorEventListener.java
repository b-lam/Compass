package io.github.b_lam.compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Brandon on 5/17/2016.
 */

public class MySensorEventListener implements SensorEventListener{

    SensorManager sensorManager;
    float[] gravity = {0,0,0}; float[] geomagnetic = {0,0,0}; float[] orientation = {0,0,0};
    float currentDegree = 0f, degree = 0f;
    float[] R = new float[9]; float[] I = new float[9];
    ImageView imgCompass;
    TextView tvDegree;
    String heading;

    public MySensorEventListener(SensorManager sensorManager, ImageView imgCompass, TextView tvDegree) {
        this.sensorManager = sensorManager;
        this.imgCompass = imgCompass;
        this.tvDegree = tvDegree;
    }

    public void onAccuracyChanged(Sensor s, int i) {}

    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            gravity[0] = Constant.ALPHA * gravity[0] + (1 - Constant.ALPHA) * event.values[0];
            gravity[1] = Constant.ALPHA * gravity[1] + (1 - Constant.ALPHA) * event.values[1];
            gravity[2] = Constant.ALPHA * gravity[2] + (1 - Constant.ALPHA) * event.values[2];

        }
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            geomagnetic[0] = Constant.ALPHA * geomagnetic[0] + (1 - Constant.ALPHA) * event.values[0];
            geomagnetic[1] = Constant.ALPHA * geomagnetic[1] + (1 - Constant.ALPHA) * event.values[1];
            geomagnetic[2] = Constant.ALPHA * geomagnetic[2] + (1 - Constant.ALPHA) * event.values[2];

        }

        if(gravity != null && geomagnetic != null){
            if(sensorManager.getRotationMatrix(R,I, gravity, geomagnetic)){
                sensorManager.getOrientation(R, orientation);

                degree = (float)Math.toDegrees(orientation[0]);
                degree = (degree+360)%360;

                RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(500);
                ra.setFillAfter(true);
                imgCompass.startAnimation(ra);
                currentDegree = -degree;
            }

        }

        if (degree >= 338 || degree < 23){
            //GOING NORTH
            heading = "N";
        }
        else if (degree >= 23 && degree < 68){
            //GOING NORTH EAST
            heading = "NE";
        }
        else if (degree >= 68 && degree < 113){
            //GOING EAST
            heading = "E";
        }
        else if (degree >= 113 && degree < 158){
            //GOING SOUTH EAST
            heading = "SE";
        }
        else if (degree >= 158 && degree < 203){
            //GOING SOUTH
            heading = "S";
        }
        else if (degree >= 203 && degree < 248){
            //GOING SOUTH WEST
            heading = "SW";
        }
        else if (degree >= 248 && degree < 293){
            //GOING WEST
            heading = "W";
        }
        else if (degree >= 293 && degree < 338){
            //GOING NORTH WEST
            heading = "NW";
        }

        tvDegree.setText(Math.round(degree) + "° " + heading);

    }

}

