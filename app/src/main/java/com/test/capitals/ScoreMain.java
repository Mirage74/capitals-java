package com.test.capitals;

import static com.test.capitals.MainActivity.BEST_SCORE;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.LAST_RESULT;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_ANSWER;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class ScoreMain extends AppCompatActivity {
    TextView tvQuizNum, tvCountryName, tvScorePoints;
    int bestScore, currScore;
    String lastQuestResult, s;
    ArrayList<UserAnswer> testState = new ArrayList<>();
    UserAnswer userAnswer;
    ArrayList<CountryDescribe> countryList;
    TableLayout tableLayout;
    int smallTextSize =0;
    Button buttonBack;
    TextView tvScore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_main);
        buttonBack = findViewById(R.id.back);
        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.back) {
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
            } else {
                Log.i("caps",  "ID textView Check : " + v.getId());
                Intent intent = new Intent(this, CheckWrongAnswer.class);
                UserAnswer ua = testState.get(v.getId());
                intent.putExtra(USER_ANSWER, ua);
                startActivity(intent);
            }
        };

        buttonBack.setOnClickListener(onClickListener);
        bestScore = loadBestScore();
        lastQuestResult = loadLastResult();
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }


        parseToUserAnswer(lastQuestResult);
        currScore = testState.stream().mapToInt(e -> e.score).sum();

        tableLayout = findViewById(R.id.layoutScoreTable);
        tvScore = findViewById(R.id.scoreText);

        s = "Score : " + currScore + " / Best score : " + bestScore;
        tvScore.setText(s);
        tableLayout.setStretchAllColumns(true);


        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);

        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        String colorRight = getResources().getString(R.string.scoreRight);
        String colorWrong = getResources().getString(R.string.scoreNull);



        TableLayout.LayoutParams trParams = new
                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                bottomRowMargin);





        for (int k = 0; k < testState.size(); k++) {
            int finalK = k;
            userAnswer = testState.stream().filter(e -> e.answerNum == finalK + 1).findFirst().get();

            tvQuizNum = new TextView(this);
            tvQuizNum.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvQuizNum.setGravity(Gravity.RIGHT);
            tvQuizNum.setPadding(1, 15, 0, 15);
            s = "" + userAnswer.answerNum + "   ";
            tvQuizNum.setText(s);
            tvQuizNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

            tvCountryName = new TextView(this);
            tvCountryName.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCountryName.setGravity(Gravity.LEFT);
            tvCountryName.setPadding(5, 15, 0, 15);
            s = "" + userAnswer.countryName;
            tvCountryName.setText(s);
            tvCountryName.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);


            TextView tvCheck = new TextView(this);
            tvCheck.setId(k + 1);
            tvCheck.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCheck.setGravity(Gravity.LEFT);
            tvCheck.setPadding(5, 15, 0, 15);
            tvCheck.setText(R.string.check);
            tvCheck.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            if (userAnswer.score > 0) {
                tvCheck.setVisibility(View.INVISIBLE);
            } else {
                tvCheck.setOnClickListener(onClickListener);
            }




            tvScorePoints = new TextView(this);
            tvScorePoints.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvScorePoints.setGravity(Gravity.LEFT);
            tvScorePoints.setPadding(5, 15, 1, 15);
            s = "" + userAnswer.score;
            tvScorePoints.setText(s);
            if (userAnswer.score > 0) {
                tvScorePoints.setTextColor(Color.parseColor(colorRight));
            } else {
                tvScorePoints.setTextColor(Color.parseColor("" + colorWrong));
            }
            tvScorePoints.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);


            final TableRow tr = new TableRow(this);
            tr.setId(k + 1);

            tr.setPadding(0,0,0,0);
            tr.setLayoutParams(trParams);
            tr.addView(tvQuizNum);
            tr.addView(tvCountryName);
            tr.addView(tvCheck);


            tr.addView(tvScorePoints);

//            if (i > -1) {
//                tr.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        TableRow tr = (TableRow) v;
//                    }
//                });
//            }

        tableLayout.addView(tr);
        }

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
