package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
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

public class Info extends AppCompatActivity {

    Button buttonScore, buttonCapsList, buttonBack;
    String userName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        buttonScore = findViewById(R.id.viewResult);
        buttonCapsList = findViewById(R.id.capsList);
        buttonBack = findViewById(R.id.back);
        userName = loadDataUser();
        TextView textView = findViewById(R.id.username);
        String text = "Hello, " + userName + "!";
        textView.setText(text);

        final ArrayList<CountryDescribe> countryList;
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }



        View.OnClickListener onClickListener = v -> {
            //int id =v.getId();
            if (v.getId() == R.id.viewResult) {
                Intent intentLR = new Intent(this, ScoreMain.class);
                intentLR.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentLR);
            } else if (v.getId() == R.id.capsList) {
                Intent intentCL = new Intent(this, CapitalsList.class);
                intentCL.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentCL);
            } else if (v.getId() == R.id.back) {
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
            }
        };
        buttonScore.setOnClickListener(onClickListener);
        buttonCapsList.setOnClickListener(onClickListener);
        buttonBack.setOnClickListener(onClickListener);
    }
    public String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }
}
