package io.github.b_lam.compass;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Compass extends AppCompatActivity {

    SensorManager sensorManager;
    SensorEventListener eListener;
    Sensor accelerometer, magFieldSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        LinearLayout l = (LinearLayout) findViewById(R.id.Layout);
        l.setOrientation(LinearLayout.VERTICAL);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        ImageView imgCompass = new ImageView(getApplicationContext());
        imgCompass.setImageResource(R.drawable.compass);
        imgCompass.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgCompass.setAdjustViewBounds(true);

        TextView tvDegree = new TextView(getApplicationContext());
        tvDegree.setTextSize(35);
        tvDegree.setGravity(Gravity.CENTER_HORIZONTAL);
        tvDegree.setTextColor(Color.WHITE);
        tvDegree.setPaddingRelative(0,150,0,0);

        //Add views to layout
        l.addView(imgCompass);
        l.addView(tvDegree);


        //Creating senor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Getting sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //Creating sensor event listener
        //Register sensors with event listener
        eListener = new MySensorEventListener(sensorManager, imgCompass, tvDegree);
        sensorManager.registerListener(eListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(eListener, magFieldSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(eListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(eListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(eListener, magFieldSensor, SensorManager.SENSOR_DELAY_GAME);
    }
}
