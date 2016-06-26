package io.github.b_lam.compass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Compass extends AppCompatActivity {

    SensorManager sensorManager;
    SensorEventListener eListener;
    Sensor accelerometer, magFieldSensor;
    AlertDialog dialogBuilder;
    TextView tvMag;
    LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        l = (LinearLayout) findViewById(R.id.Layout);
        l.setOrientation(LinearLayout.VERTICAL);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        ImageView imgCompass = new ImageView(getApplicationContext());
        imgCompass.setImageResource(R.drawable.compass);
        imgCompass.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgCompass.setAdjustViewBounds(true);

        TextView tvDegree = new TextView(getApplicationContext());
        tvDegree.setTextSize(35);
        tvDegree.setGravity(Gravity.CENTER_HORIZONTAL);
        tvDegree.setTextColor(Color.WHITE);
        tvDegree.setPaddingRelative(0,100,0,0);

        //Add views to layout
        l.addView(imgCompass);
        l.addView(tvDegree);

        tvMag = new TextView(getApplicationContext());
        tvMag.setGravity(Gravity.CENTER_HORIZONTAL);
        tvMag.setTextColor(Color.WHITE);
        tvMag.setTextSize(18);

        SharedPreferences sharedPreferences = getSharedPreferences("enable", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("magField", false)){
            l.addView(tvMag);
        }

        //Creating senor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Getting sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //Creating sensor event listener
        //Register sensors with event listener
        eListener = new MySensorEventListener(sensorManager, imgCompass, tvDegree, tvMag);
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

        SharedPreferences sharedPreferences = getSharedPreferences("enable", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("magField", false) && tvMag.getParent() == null) {
            l.addView(tvMag);
        }else if(!sharedPreferences.getBoolean("magField", false) && tvMag.getParent() != null){
            l.removeView(tvMag);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_calibrate:
                calibrateDialog();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void calibrateDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View calibrateView = layoutInflater.inflate(R.layout.calibrate_dialog_layout, null);

        dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.setTitle("How to calibrate");
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setView(calibrateView);

        Button btnClose = (Button) calibrateView.findViewById(R.id.close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.show();
    }
}
