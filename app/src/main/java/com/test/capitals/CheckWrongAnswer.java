package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.USER_ANSWER;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class  CheckWrongAnswer extends AppCompatActivity {
    UserAnswer userAnswer;
    Button buttonBack;
    String s;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_wrong_answer);
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            userAnswer = intent.getSerializableExtra(USER_ANSWER, UserAnswer.class);
        } else {
            userAnswer = (UserAnswer) intent.getSerializableExtra(USER_ANSWER);
        }

        TextView tvCountry  = findViewById(R.id.country_name);
        s = "Country name : " + userAnswer.countryName;
        tvCountry.setText(s);
        buttonBack = findViewById(R.id.back);

        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.back) {
                Intent intentBack = new Intent(this, ScoreMain.class);
                startActivity(intentBack);
            }
        };
        buttonBack.setOnClickListener(onClickListener);
    }
}
