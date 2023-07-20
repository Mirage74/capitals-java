package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT;
import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT_PCT_EASY;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.EXTRAS_DIFFICULT_LVL;
import static com.test.capitals.MainActivity.EXTRAS_USER_TEST_CURRENT_INFO;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class CoreTest extends AppCompatActivity {
    ArrayList<CountryDescribe> countryList, countryListCut, countryListNew;
    ArrayList<Object> testState;
    Button buttonCap1, buttonCap2, buttonCap3, buttonCap4;
    ImageView imageView;
    String userName;
    CountryDescribe testCountry;
    TextView textViewUsername, textViewCountryName, testViewQuestionNumber;
    int allQuestions, scoreEasy, scoreMedium, scoreHard, pctEasy, diffLvl;
    int timeBestScoreSec, timeMediumScoreSec, timeMaxSec, maxEasyPct;
    private static final String GET_IMAGE_URL = "http://10.0.2.2:4000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core_test);
        Resources resources = getResources();
        userName = loadDataUser();
        buttonCap1 = findViewById(R.id.caital1);
        buttonCap2 = findViewById(R.id.caital2);
        buttonCap3 = findViewById(R.id.caital3);
        buttonCap4 = findViewById(R.id.caital4);

        textViewUsername = findViewById(R.id.username);
        textViewUsername.setText(userName);
        textViewCountryName = findViewById(R.id.country_name);
        testViewQuestionNumber = findViewById(R.id.question_number);

        imageView = findViewById(R.id.image);
        Intent intent = getIntent();
        //pctEasy = intent.getIntExtra(EXTRAS_COUNTRY_CURRENT_PCT_EASY, 0);
        diffLvl = intent.getIntExtra(EXTRAS_DIFFICULT_LVL, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
            countryListCut = intent.getParcelableArrayListExtra(EXTRAS_COUNTRY_CURRENT, CountryDescribe.class);
            testState = intent.getParcelableArrayListExtra(EXTRAS_USER_TEST_CURRENT_INFO, Object.class);

        } else {
            countryList = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
            countryListCut = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTRY_CURRENT);
            testState = (ArrayList<Object>)intent.getSerializableExtra(EXTRAS_USER_TEST_CURRENT_INFO);
        }

        if (diffLvl == 0) {
            scoreEasy = resources.getInteger(R.integer.easy_points_min);
            scoreMedium = resources.getInteger(R.integer.easy_points_medium);
            scoreHard = resources.getInteger(R.integer.easy_points_max);
        } else if (diffLvl == 1) {
            scoreEasy = resources.getInteger(R.integer.medium_points_min);
            scoreMedium = resources.getInteger(R.integer.medium_points_medium);
            scoreHard = resources.getInteger(R.integer.medium_points_max);
        } else {
            scoreEasy = resources.getInteger(R.integer.hard_points_min);
            scoreMedium = resources.getInteger(R.integer.hard_points_medium);
            scoreHard = resources.getInteger(R.integer.hard_points_max);
        }
        allQuestions = resources.getInteger(R.integer.test_questions);
        timeBestScoreSec = resources.getInteger(R.integer.time_score_best);
        timeMediumScoreSec = resources.getInteger(R.integer.time_score_medium);
        timeMaxSec = resources.getInteger(R.integer.max_time_test_sec);
        maxEasyPct = resources.getInteger(R.integer.min_pct_hard);

        Random rand = new Random();
        int int_random = rand.nextInt(countryListCut.size());
        testCountry = countryListCut.get(int_random);
        System.out.println("testCountry : " + testCountry);
        textViewCountryName.setText(testCountry.countryName);
        String s = testState.size() + 1 + " / " + allQuestions;
        testViewQuestionNumber.setText(s);
        int_random = rand.nextInt(countryList.size());
        buttonCap1.setText(countryList.get(int_random).capitalName);
        int_random = rand.nextInt(countryList.size());
        buttonCap2.setText(countryList.get(int_random).capitalName);
        int_random = rand.nextInt(countryList.size());
        buttonCap3.setText(countryList.get(int_random).capitalName);
        int_random = rand.nextInt(countryList.size());
        buttonCap4.setText(countryList.get(int_random).capitalName);
        Picasso.get().load(GET_IMAGE_URL + testCountry.imageName).into(imageView);


        System.out.println("countryListCut : " + countryListCut);
        System.out.println("pctEasy : " + pctEasy);
        System.out.println("testState : " + testState);
    }
    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }
}

