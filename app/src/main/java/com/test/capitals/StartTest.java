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
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StartTest extends AppCompatActivity {

    String userName;
    Button buttonEasy, buttonMedium, buttonHard;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_test);
        buttonEasy = findViewById(R.id.easy);
        buttonMedium = findViewById(R.id.medium);
        buttonHard = findViewById(R.id.hard);

        Intent intent = getIntent();
        ArrayList<CountryDescribe> countryList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }

        userName = loadDataUser();
        TextView textView = findViewById(R.id.tv1);
        String text = "Hello, " + userName + " !";
        textView.setText(text);


        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.easy) {
                ArrayList<CountryDescribe> countryListCut = filterCountryByDiffLvl(countryList, 2);
                System.out.println("countryListCut size: " + countryListCut.size());
                Intent intentRunTest = new Intent("android.intent.action.core-test-capitals");
                intentRunTest.putExtra(EXTRAS_COUNTY_LIST, countryList);
                intentRunTest.putExtra(EXTRAS_COUNTRY_CURRENT, countryListCut);
                //intentRunTest.putExtra(EXTRAS_COUNTRY_CURRENT_PCT_EASY, 0);
                ArrayList<Object> testState = new ArrayList<>();
                intentRunTest.putExtra(EXTRAS_USER_TEST_CURRENT_INFO, testState);
                intentRunTest.putExtra(EXTRAS_DIFFICULT_LVL, 0);

                startActivity(intentRunTest);
            } else if (v.getId() == R.id.info) {

            } else if (v.getId() == R.id.about) {

            }
        };

        buttonEasy.setOnClickListener(onClickListener);
        buttonMedium.setOnClickListener(onClickListener);
        buttonHard.setOnClickListener(onClickListener);

    }

    private ArrayList filterCountryByDiffLvl(ArrayList<CountryDescribe> countryList, int diffLvl) {
        return (ArrayList)countryList.stream().filter(e -> e.diffLvl < diffLvl).collect(Collectors.toList());
    }
    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }
}
