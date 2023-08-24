package com.test.capitals;

import static com.test.capitals.CoreTest.NO_ANSWER_TIME_EXPIRED;
//import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT;
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

import java.util.ArrayList;

public class  CheckWrongAnswer extends AppCompatActivity {
    UserAnswer userAnswer;
    Button buttonBack;
    ArrayList<CountryDescribe> countryList;
    TextView tvCountry, tvRightAnswer, tvYourAnswer;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.i("alc",  "Check WrongAnswer onCreate, userAnswer  : " + userAnswer);

        setContentView(R.layout.check_wrong_answer);
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
            userAnswer = intent.getSerializableExtra(USER_ANSWER, UserAnswer.class);
        } else {
            userAnswer = (UserAnswer) intent.getSerializableExtra(USER_ANSWER);
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }

        tvCountry  = findViewById(R.id.country_name);
        tvRightAnswer  = findViewById(R.id.rightAnswer);
        tvYourAnswer  = findViewById(R.id.yourAnswer);
        String s1 = "Country : " + userAnswer.countryName;
        tvCountry.setText(s1);
        String s2 = "Capital : " + userAnswer.capitalName;
        tvRightAnswer.setText(s2);
        String s3 = userAnswer.userAnswer;
        if (s3.equals(NO_ANSWER_TIME_EXPIRED)) {
            s3 = "Time expired, no answer";
        } else {
            s3 = "Your answer : " + userAnswer.userAnswer;
        }
        tvYourAnswer.setText(s3);

        buttonBack = findViewById(R.id.back);

        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.back) {
                Intent intentBack = new Intent(this, ScoreMain.class);
                intentBack.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentBack);
            }
        };
        buttonBack.setOnClickListener(onClickListener);
    }


//    @Override
//    public void onStart(){
//        super.onStart();
//        Log.i("alc",  "Check WrongAnswer onStart, userAnswer  : " + userAnswer);
//    }
//
//    @Override
//    public void onRestart(){
//        super.onRestart();
//        Log.i("alc",  "Check WrongAnswer onRestart, userAnswer  : " + userAnswer);
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        Log.i("alc",  "Check WrongAnswer onResume, userAnswer  : " + userAnswer);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.i("alc",  "Check WrongAnswer onPause, userAnswer  : " + userAnswer);
//        CheckWrongAnswer.this.finish();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.i("alc", "Check WrongAnswer onStop, userAnswer  : " + userAnswer);
//    }
//
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        Log.i("alc",  "Check WrongAnswer onDestroy, userAnswer  : " + userAnswer);
//    }
}