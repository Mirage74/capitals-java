package com.test.capitals;

//import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static com.test.capitals.MainActivity.BACKEND_URL;
import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT;
import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT_ALL;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.EXTRAS_DIFFICULT_LVL;
import static com.test.capitals.MainActivity.EXTRAS_USER_TEST_CURRENT_INFO;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;



public class CoreTest extends AppCompatActivity {
    static boolean timeExpire = false;
    ArrayList<CountryDescribe> countryList, countryListCut, countryListCutAll, countryListTemp;
    ArrayList<Object> testState;
    Button buttonCap1, buttonCap2, buttonCap3, buttonCap4;
    String userName;
    CountryDescribe testCountry;
    TextView textViewUsername;
    TextView textViewCountryName;
    TextView testViewQuestionNumber;

    public void setTextViewSecValue(String sec) {
        this.textViewSec.setText(sec);
    }

    TextView textViewSec;
    int allQuestions, scoreEasy, scoreMedium, scoreHard, diffLvl;
    int timeBestScoreSec, timeMediumScoreSec, timeMaxSec, currTime, rightAnswerNum;
    String[] answerVariants = new String[4];
    Random rand = new Random();
    Timer timer = new Timer();


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
        textViewSec = findViewById(R.id.sec_rest);

        //imageView = findViewById(R.id.image);
        Intent intent = getIntent();
        //pctEasy = intent.getIntExtra(EXTRAS_COUNTRY_CURRENT_PCT_EASY, 0);
        diffLvl = intent.getIntExtra(EXTRAS_DIFFICULT_LVL, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
            countryListCut = intent.getParcelableArrayListExtra(EXTRAS_COUNTRY_CURRENT, CountryDescribe.class);
            countryListCutAll = intent.getParcelableArrayListExtra(EXTRAS_COUNTRY_CURRENT_ALL, CountryDescribe.class);
            testState = intent.getParcelableArrayListExtra(EXTRAS_USER_TEST_CURRENT_INFO, Object.class);

        } else {
            countryList = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
            countryListCut = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTRY_CURRENT);
            countryListCutAll = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTRY_CURRENT_ALL);
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



        int int_random;


        int_random = rand.nextInt(countryListCut.size());
        testCountry = countryListCut.get(int_random);
        System.out.println("testCountry : " + testCountry);
        textViewCountryName.setText(testCountry.countryName);
        String s = testState.size() + 1 + " / " + allQuestions;
        testViewQuestionNumber.setText(s);
//        s = Integer.toString(timeMaxSec);
//        textViewSec.setText(s);
        countryListCut.remove(int_random);
        System.out.println("countryListCut.size() : " + countryListCut.size());



        countryListTemp = new ArrayList<>();
        countryListTemp = (ArrayList)countryList.clone();
        rightAnswerNum = rand.nextInt(3);
        answerVariants[rightAnswerNum] = testCountry.capitalName;
        System.out.println("rightAnswerNum : " + rightAnswerNum);

        countryListTemp = (ArrayList)countryList.stream().filter(e -> e.id != testCountry.id).collect(Collectors.toList());
        FillAnswer();
        FillAnswer();
        FillAnswer();

        System.out.println("answerVariants : " + Arrays.toString(answerVariants));
        System.out.println("countryListtemp len : " + countryListTemp.size());
        System.out.println("countryListCut len0 : " + countryListCut.size());
        System.out.println("countryList len0 : " + countryList.size());


        buttonCap1.setText(answerVariants[0]);
        buttonCap2.setText(answerVariants[1]);
        buttonCap3.setText(answerVariants[2]);
        buttonCap4.setText(answerVariants[3]);
        Picasso.get().load(BACKEND_URL + "/" + testCountry.imageName).into(ivImageCapital);
        currTime = timeMaxSec;
        TimerTask timerTask = new TimerTaskSecAnswer();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);




    }
    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }

    private void FillAnswer() {
        boolean found = false;
        int i = 0;
        int randomNum  = rand.nextInt(countryListTemp.size());
        while (!found) {
            if (answerVariants[i] == null) {
                answerVariants[i] = countryListTemp.get(randomNum).capitalName;
                found = true;
            } else {
                i++;
            }
        }
        ArrayList<CountryDescribe> cL = new ArrayList<>();
        cL = (ArrayList)countryListTemp.stream().filter(e -> e.id != countryListTemp.get(randomNum).id).collect(Collectors.toList());
        countryListTemp = (ArrayList)cL.clone();
    }
    class TimerTaskSecAnswer extends TimerTask {
        @Override
        public void run() {
//        System.out.println("currTime : " + currTime);
            String s = Integer.toString(currTime);
            new Handler(Looper.getMainLooper()).post(() -> setTextViewSecValue(s));
            //setTextViewSecValue(s);
            currTime -= 1;
            if (currTime == -1) {
                new Handler(Looper.getMainLooper()).post(() -> setTextViewSecValue("0"));
                CoreTest.timeExpire = true;
                timer.cancel();
            }
        }
    }
}



