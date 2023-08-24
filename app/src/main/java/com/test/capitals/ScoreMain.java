package com.test.capitals;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static com.test.capitals.MainActivity.BEST_RESULT;
import static com.test.capitals.MainActivity.BEST_SCORE;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.LAST_RESULT;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import java.util.ArrayList;

public class ScoreMain extends AppCompatActivity {
    public static final int LAST_SCORE_MODE = 1;
    public static final int BEST_SCORE_MODE = 2;
    public static final String ScoreFragmentTag = "ScoreFragmentTag";
    Fragment fragment;
    FrameLayout frameLayout;
    FragmentTransaction ft;
    FragmentManager fm = getSupportFragmentManager();
    TextView tvCongratulation;
    int bestScore, currScore, currViewMode = LAST_SCORE_MODE;
    String lastQuestResult, bestQuestResult, s;
    ArrayList<UserAnswer> testState = new ArrayList<>();
    UserAnswer userAnswer;
    ArrayList<CountryDescribe> countryList;
    LinearLayout linearLayout;
    int smallTextSize, buttonTextSize;
    Button buttonSwitch, buttonBack;
    TextView tvScore;
    String userName, colorRight, colorWrong;
    ViewGroup.LayoutParams params;
    View fragView;

    private void newFragment() {
        fragment = new com.test.capitals.LastResFragment();
        ft = fm.beginTransaction().setReorderingAllowed(true);
        ft.replace(R.id.fragLastRes, fragment, ScoreFragmentTag);
        ft.commit();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_main);
        linearLayout = findViewById(R.id.layoutScore);
        params = linearLayout.getLayoutParams();
        //Log.i("alc",  "ScoreMain orientation  : " + getResources().getConfiguration().orientation);

        userName = loadDataUser();
        buttonBack = findViewById(R.id.back);
        buttonSwitch = findViewById(R.id.switchRes);
        frameLayout = findViewById(R.id.fragLastRes);
        View.OnClickListener onClickListener = v -> {
            //int id =v.getId();
            if (v.getId() == R.id.back) {
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
            } else if (v.getId() == R.id.switchRes) {
                if (currViewMode == LAST_SCORE_MODE) {
                    s = "Best score : " + bestScore;
                    tvScore.setText(s);
                    buttonSwitch.setText(R.string.view_last_result);
                    currViewMode = BEST_SCORE_MODE;
                    frameLayout.removeAllViews();
                    buttonSwitch.setVisibility(View.INVISIBLE);
                    buttonBack.setVisibility(View.INVISIBLE);
                    newFragment();
                } else {
                    s = "Last score : " + currScore;
                    tvScore.setText(s);
                    buttonSwitch.setText(R.string.view_best_result);
                    currViewMode = LAST_SCORE_MODE;
                    frameLayout.removeAllViews();
                    buttonSwitch.setVisibility(View.INVISIBLE);
                    buttonBack.setVisibility(View.INVISIBLE);
                    newFragment();
                }
            }
        };

        buttonBack.setOnClickListener(onClickListener);
        buttonSwitch.setOnClickListener(onClickListener);
        bestScore = loadBestScore();
        lastQuestResult = loadLastResult();
        bestQuestResult = loadBestResult();
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }
        testState = parseStringToUserAnswerList(lastQuestResult, countryList);
        currScore = testState.stream().mapToInt(e -> e.score).sum();

        tvScore = findViewById(R.id.scoreText);
        tvCongratulation = findViewById(R.id.congratulation);

        //s = "Score : " + currScore + " / Best score : " + bestScore;
        s = "Last score : " + currScore;
        tvScore.setText(s);
        s = "Congratulation, " + userName + " !";
        tvCongratulation.setText(s);


        newFragment();

//        fragment = new com.test.capitals.LastResFragment();
//        fm = getSupportFragmentManager();
//        ft = fm.beginTransaction().setReorderingAllowed(true);
//        ft.replace(R.id.fragLastRes, fragment, ScoreFragmentTag);
//        ft.commit();


        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        buttonTextSize = getResources().getInteger(R.integer.buttonCheckTextSize);
        colorRight = getResources().getString(R.string.scoreRight);
        colorWrong = getResources().getString(R.string.scoreNull);

    }

    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }

    public static ArrayList<UserAnswer> parseStringToUserAnswerList(String jsonAnswer, ArrayList<CountryDescribe> countryList) {
        String s = jsonAnswer;
        ArrayList<UserAnswer> tS = new ArrayList<>();
        while (s.length() > 5) {
            int i = s.indexOf("}");
            String temp = s.substring(0, i + 1);
            tS.add(new UserAnswer(temp, countryList));
            s = s.substring(i + 1);
        }
        return tS;
    }
    private int loadBestScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(BEST_SCORE, 0);
    }
    private String loadLastResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //String s = sharedPreferences.getString(LAST_RESULT, "0");
        return sharedPreferences.getString(LAST_RESULT, "0");
    }

    private String loadBestResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String s = sharedPreferences.getString(BEST_RESULT, "0");
        return sharedPreferences.getString(BEST_RESULT, "0");
    }




    public void onResume(Bundle savedInstanceState) {
        super.onResume();
        Log.i("alc",  "ScoreMain onResume");


    }

    public void onStart(Bundle savedInstanceState) {
        super.onStart();
        Log.i("alc",  "ScoreMain onStart");
    }

    public void onPause(Bundle savedInstanceState) {
        super.onPause();
        Log.i("alc",  "ScoreMain onPause");
    }

    public void onStop(Bundle savedInstanceState) {
        super.onStop();
        Log.i("alc",  "ScoreMain onStop");
    }

}
