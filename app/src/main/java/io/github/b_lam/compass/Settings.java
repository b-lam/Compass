package io.github.b_lam.compass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final CheckBox cbEnableMag = (CheckBox) findViewById(R.id.enableMagField);

        SharedPreferences sharedPreferences = getSharedPreferences("enable", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("magField", false)){
            cbEnableMag.setChecked(true);
        }else{
            cbEnableMag.setChecked(false);
        }

        cbEnableMag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbEnableMag.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences("enable", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("magField", true);
                    editor.apply();
//                    Toast.makeText(Settings.this, "Hi", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences("enable", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("magField", false);
                    editor.apply();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
