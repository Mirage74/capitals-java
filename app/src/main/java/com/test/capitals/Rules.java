package com.test.capitals;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Rules extends AppCompatActivity {
    int scoreFastEasy, scoreMediumEasy, scoreSlowEasy, scoreFastMedium, scoreMediumMedium, scoreSlowMedium,
            scoreFastHard, scoreMediumHard, scoreSlowHard, allQuestions,
            timeBestScoreSec, timeMediumScoreSec, timeMaxSec;
    TextView tvTakePart, tvGroupDiff, tvSecBestScore, tvSecMediumScore, tvSecMinScore,
        tvPointsEasyFast, tvPointsEasyMedium, tvPointsEasySlow,
        tvPointsMediumFast, tvPointsMediumMedium, tvPointsMediumSlow,
        tvPointsHardFast, tvPointsHardMedium, tvPointsHardSlow;
    String s;
    int i, j;
    ConstraintLayout constraintLayout;

    Button buttonBack;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);
        Resources resources = getResources();
        buttonBack = findViewById(R.id.back);
        constraintLayout = findViewById(R.id.rulesLayout);

        tvTakePart = findViewById(R.id.tvTakePart);
        tvGroupDiff = findViewById(R.id.tvGroupDiff);
        tvSecBestScore = findViewById(R.id.easyBest);
        tvSecMediumScore = findViewById(R.id.easyMedium);
        tvSecMinScore = findViewById(R.id.easyHard);
        tvPointsEasyFast = findViewById(R.id.easyFastScore);
        tvPointsEasyMedium = findViewById(R.id.easyMediumScore);
        tvPointsEasySlow = findViewById(R.id.easySlowScore);
        tvPointsMediumFast = findViewById(R.id.mediumFastScore);
        tvPointsMediumMedium = findViewById(R.id.mediumMediumScore);
        tvPointsMediumSlow = findViewById(R.id.mediumSlowScore);
        tvPointsHardFast = findViewById(R.id.hardFastScore);
        tvPointsHardMedium = findViewById(R.id.hardMediumScore);
        tvPointsHardSlow = findViewById(R.id.hardSlowScore);



        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.back) {
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
            }
        };
        buttonBack.setOnClickListener(onClickListener);

        allQuestions = resources.getInteger(R.integer.test_questions);

        timeBestScoreSec = resources.getInteger(R.integer.time_score_best);
        timeMediumScoreSec = resources.getInteger(R.integer.time_score_medium);
        timeMaxSec = resources.getInteger(R.integer.max_time_test_sec);

        scoreSlowEasy = resources.getInteger(R.integer.easy_points_min);
        scoreMediumEasy = resources.getInteger(R.integer.easy_points_medium);
        scoreFastEasy = resources.getInteger(R.integer.easy_points_max);
        scoreSlowMedium = resources.getInteger(R.integer.medium_points_min);
        scoreMediumMedium = resources.getInteger(R.integer.medium_points_medium);
        scoreFastMedium = resources.getInteger(R.integer.medium_points_max);
        scoreSlowHard = resources.getInteger(R.integer.hard_points_min);
        scoreMediumHard = resources.getInteger(R.integer.hard_points_medium);
        scoreFastHard = resources.getInteger(R.integer.hard_points_max);

        s = "   Take part in a " + allQuestions + "-" + tvTakePart.getText();
        tvTakePart.setText(s);
        i = timeMaxSec - timeBestScoreSec;
        s = "< " + i;
        tvSecBestScore.setText(s);
        i = timeMaxSec - timeBestScoreSec;
        j = timeMaxSec - timeMediumScoreSec;
        s = i + "-" + j;
        tvSecMediumScore.setText(s);
        i = timeMaxSec - timeMediumScoreSec;
        s = i + "-" + timeMaxSec;
        tvSecMinScore.setText(s);
        s = "" + scoreFastEasy;
        tvPointsEasyFast.setText(s);
        s = "" + scoreMediumEasy;
        tvPointsEasyMedium.setText(s);
        s = "" + scoreSlowEasy;
        tvPointsEasySlow.setText(s);
        s = "" + scoreFastMedium;
        tvPointsMediumFast.setText(s);
        s = "" + scoreMediumMedium;
        tvPointsMediumMedium.setText(s);
        s = "" + scoreSlowMedium;
        tvPointsMediumSlow.setText(s);
        s = "" + scoreFastHard;
        tvPointsHardFast.setText(s);
        s = "" + scoreMediumHard;
        tvPointsHardMedium.setText(s);
        s = "" + scoreSlowHard;
        tvPointsHardSlow.setText(s);

        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            constraintLayout.removeView(tvTakePart);
            constraintLayout.removeView(tvGroupDiff);
        }
    }
}
