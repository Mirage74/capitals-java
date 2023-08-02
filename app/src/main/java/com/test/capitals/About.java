package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class About extends AppCompatActivity {
    Button buttonBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        //Intent intent = getIntent();
        buttonBack = findViewById(R.id.back);
        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.back) {
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
            }
        };

        buttonBack.setOnClickListener(onClickListener);
    }
}
