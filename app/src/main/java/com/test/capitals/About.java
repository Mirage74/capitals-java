package com.test.capitals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    Button buttonBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
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
