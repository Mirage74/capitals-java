package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LoggedUser extends AppCompatActivity {
    String userName;
    Button buttonStartTest, buttonInfo, buttonAbout, buttonLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.i("alc",  "LoggedUser onCreate");

        setContentView(R.layout.logged_user);
        buttonStartTest = findViewById(R.id.start_test);
        buttonInfo = findViewById(R.id.info);
        buttonAbout = findViewById(R.id.about);
        buttonLogout = findViewById(R.id.logout);
        Intent intent = getIntent();
        final ArrayList<CountryDescribe> countryList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }
        userName = loadDataUser();
        TextView textView = findViewById(R.id.username);
        String text = "Hello, " + userName + "!";
        textView.setText(text);


        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.start_test) {
                Intent intentStartTest = new Intent(this, StartTest.class);
                intentStartTest.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentStartTest);
            } else if (v.getId() == R.id.info) {

            } else if (v.getId() == R.id.about) {
                Intent intentAbout = new Intent(this, About.class);
                intentAbout.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentAbout);
            } else if (v.getId() == R.id.logout) {
                saveUser(NOT_LOGGED_USER);
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
            }
        };

        buttonStartTest.setOnClickListener(onClickListener);
        buttonInfo.setOnClickListener(onClickListener);
        buttonAbout.setOnClickListener(onClickListener);
        buttonLogout.setOnClickListener(onClickListener);
    }

    @Override
    public void onStart(){
        super.onStart();
        //Log.i("alc",  "LoggedUser onStart");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        //Log.i("alc",  "LoggedUser onRestart");
    }

    @Override
    public void onResume(){
        super.onResume();
        //Log.i("alc",  "LoggedUser onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.i("alc",  "LoggedUser onPause");
    }

    @Override
    public void onStop(){
        super.onStop();
        //Log.i("alc",  "LoggedUser onStop");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Log.i("alc",  "LoggedUser onDestroy");
    }


    public void saveUser(String userName) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
        Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
    }
    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }
}
