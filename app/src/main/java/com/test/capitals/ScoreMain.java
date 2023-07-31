package com.test.capitals;

import static com.test.capitals.MainActivity.BEST_SCORE;
import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.LAST_RESULT;
import static com.test.capitals.MainActivity.SHARED_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreMain extends AppCompatActivity {
    //TextView tvQuizNum, tvCountryName, tvScorePoints;
    int bestScore;
    String lastQuestResult, s;
    ArrayList<UserAnswer> testState = new ArrayList<>();
    UserAnswer userAnswer;
    ArrayList<CountryDescribe> countryList;
    LinearLayout linearLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_main);
//        tvQuizNum = findViewById(R.id.question_number);
//        tvCountryName = findViewById(R.id.scoreCountry);
//        tvScorePoints = findViewById(R.id.scorePoints);
        bestScore = loadBestScore();
        lastQuestResult = loadLastResult();
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }
        parseToUserAnswer(lastQuestResult);

//        s = "" + userAnswer.id;
//        tvQuizNum.setText(s);
//        tvCountryName.setText(userAnswer.countryName);
//        s = "" + userAnswer.score;
//        tvScorePoints.setText(s);

        LinearLayout currLayout = findViewById(R.id.layoutScore);

        for (int k = 0; k < testState.size(); k++) {
            int finalK = k;
            userAnswer = testState.stream().filter(e -> e.answerNum == finalK + 1).findFirst().get();
            //TableLayout
            linearLayout = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams paramsTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsTextView.setMargins(5, 3, 5, 3);
            linearLayout.setLayoutParams(paramsTextView);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackground("unsplash");
//            paramsTextView.leftMargin = 25;
//            paramsTextView.rightMargin = 25;
            //paramsTextView.weight = 1;

            TextView tvID = new TextView(getApplicationContext());
            tvID.setWidth(200);
            tvID.setTextSize(30);
            tvID.setLayoutParams(paramsTextView);
            s = "" + userAnswer.answerNum;
            tvID.setText(s);


            TextView tvCountryName = new TextView(getApplicationContext());
            tvCountryName.setWidth(700);
            tvCountryName.setTextSize(25);
            tvCountryName.setLayoutParams(paramsTextView);
            s = userAnswer.countryName;
            tvCountryName.setText(s);

            TextView tvScore = new TextView(getApplicationContext());
            tvScore.setWidth(100);
            tvScore.setTextSize(30);
            tvScore.setLayoutParams(paramsTextView);
            s = "" + userAnswer.score;
            tvScore.setText(s);

            linearLayout.addView(tvID);
            linearLayout.addView(tvCountryName);
            linearLayout.addView(tvScore);
            currLayout.addView(linearLayout);
        }


//        TextView tvScore = new TextView(getApplicationContext());
//        tvScore.setTextSize(30);
//        //tvCountryName.setLayoutParams(paramsTextView);
//        s = "" + userAnswer.score;
//        tvCountryName.setText(s);
//        linearLayout.addView(tvScore);



    }

    private void parseToUserAnswer(String jsonAnswer) {
        String s = jsonAnswer;
        while (s.length() > 5) {
            int i = s.indexOf("}");
            String temp = s.substring(0, i + 1);
            testState.add(new UserAnswer(temp, countryList));
            s = s.substring(i + 1);
        }
    }
    private int loadBestScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(BEST_SCORE, 0);
    }
    private String loadLastResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(LAST_RESULT, "0");
    }
}
