package com.test.capitals;

import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static com.test.capitals.MainActivity.BACKEND_API;
import static com.test.capitals.MainActivity.BACKEND_URL;
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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import kotlin.jvm.internal.Intrinsics;

public class CoreTest extends AppCompatActivity {
    ArrayList<CountryDescribe> countryList, countryListCut, countryListNew, countryListNew1, countryListNew2;
    ArrayList<Object> testState;
    Button buttonCap1, buttonCap2, buttonCap3, buttonCap4;
    ImageView imageView;
    String userName;
    CountryDescribe testCountry;
    TextView textViewUsername, textViewCountryName, testViewQuestionNumber;
    int allQuestions, scoreEasy, scoreMedium, scoreHard, pctEasy, diffLvl;
    int timeBestScoreSec, timeMediumScoreSec, timeMaxSec, rightAnswerNum;
    String[] answerVariants = new String[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core_test);

        ImageView ivImageCapital = findViewById(R.id.ivImageCapital);



        Resources resources = getResources();
        userName = loadDataUser();
        buttonCap1 = findViewById(R.id.capital1);
        buttonCap2 = findViewById(R.id.capital2);
        buttonCap3 = findViewById(R.id.capital3);
        buttonCap4 = findViewById(R.id.capital4);

        textViewUsername = findViewById(R.id.username);
        textViewUsername.setText(userName);
        textViewCountryName = findViewById(R.id.country_name);
        testViewQuestionNumber = findViewById(R.id.question_number);

        //imageView = findViewById(R.id.image);
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


        Random rand = new Random();
        int int_random;


        int_random = rand.nextInt(countryListCut.size());
        testCountry = countryListCut.get(int_random);
        System.out.println("testCountry : " + testCountry);
        textViewCountryName.setText(testCountry.countryName);
        String s = testState.size() + 1 + " / " + allQuestions;
        testViewQuestionNumber.setText(s);
        countryListCut.remove(int_random);
        System.out.println("countryListCut.size() : " + countryListCut.size());


        countryListNew = new ArrayList<>();
        countryListNew = (ArrayList)countryList.clone();
        rightAnswerNum = rand.nextInt(3);
        answerVariants[rightAnswerNum] = testCountry.capitalName;
        System.out.println("rightAnswerNum : " + rightAnswerNum);

        countryListNew = (ArrayList)countryList.stream().filter(e -> e.id != testCountry.id).collect(Collectors.toList());

//        countryListNew.remove(0);
//        System.out.println("countryListNew 0 : " + countryListNew.get(0));
//        System.out.println("countryListCut 0 : " + countryListCut.get(0));
//        System.out.println("countryList 0 : " + countryList.get(0));
//
//        System.out.println("countryListNew len0 : " + countryListNew.size());
//        System.out.println("countryListCut len0 : " + countryListCut.size());
//        System.out.println("countryList len0 : " + countryList.size());
        boolean found = false;
        int i = 0;
        int rand1 = rand.nextInt(countryListNew.size());
        while (!found) {
            if (answerVariants[i] == null) {
                answerVariants[i] = countryListNew.get(rand1).capitalName;
                found = true;
            } else {
                i++;
            }
        }
        countryListNew1 = new ArrayList<>();
        countryListNew1 = (ArrayList)countryListNew.stream().filter(e -> e.id != countryListNew.get(rand1).id).collect(Collectors.toList());

        found = false;
        i = 0;
        int rand2 = rand.nextInt(countryListNew1.size());
        while (!found) {
            if (answerVariants[i] == null) {
                answerVariants[i] = countryListNew.get(rand2).capitalName;
                found = true;
            } else {
                i++;
            }
        }
        countryListNew2 = new ArrayList<>();
        countryListNew2 = (ArrayList)countryListNew1.stream().filter(e -> e.id != countryListNew1.get(rand2).id).collect(Collectors.toList());

        found = false;
        i = 0;
        int rand3 = rand.nextInt(countryListNew2.size());
        while (!found) {
            if (answerVariants[i] == null) {
                answerVariants[i] = countryListNew.get(rand3).capitalName;
                found = true;
            } else {
                i++;
            }
        }

        System.out.println("answerVariants : " + Arrays.toString(answerVariants));
        System.out.println("countryListNew len0 : " + countryListNew.size());
        System.out.println("countryListNew1 len0 : " + countryListNew1.size());
        System.out.println("countryListNew2 len0 : " + countryListNew2.size());
        System.out.println("countryListCut len0 : " + countryListCut.size());
        System.out.println("countryList len0 : " + countryList.size());


        buttonCap1.setText(answerVariants[0]);
        int_random = rand.nextInt(countryList.size());
        buttonCap2.setText(answerVariants[1]);
        int_random = rand.nextInt(countryList.size());
        buttonCap3.setText(answerVariants[2]);
        int_random = rand.nextInt(countryList.size());
        buttonCap4.setText(answerVariants[3]);
        Picasso.get().load(BACKEND_URL + "/" + testCountry.imageName).into(ivImageCapital);





    }
    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }


}

