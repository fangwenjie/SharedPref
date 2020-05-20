package com.fangwenjie.sharedpref.example;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fangwenjie.sharedpref.example.shareprefs.WangZheRongYaoSharedPref;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String luban = new WangZheRongYaoSharedPref(this).luBan().get();

        ((TextView) findViewById(R.id.helloTxt)).setText(luban);

    }
}
