package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class NotLoggedUser extends AppCompatActivity {

    String userName;
    Button buttonLogin, buttonRegister, buttonInfo, buttonAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_logged_user);
        buttonLogin = findViewById(R.id.login);
        buttonRegister = findViewById(R.id.register);
        buttonInfo = findViewById(R.id.info);
        buttonAbout = findViewById(R.id.about);

        Intent intent = getIntent();
        final ArrayList<CountryDescribe> countryList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }


        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.login) {
                Intent intentLogin = new Intent("android.intent.action.login-capitals");
                intentLogin.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentLogin);
            } else if (v.getId() == R.id.register) {
                Intent intentReg = new Intent("android.intent.action.register-user-capitals");
                intentReg.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentReg);
            } else if (v.getId() == R.id.info) {

            } else if (v.getId() == R.id.about) {
                Intent intentAbout = new Intent("android.intent.action.about-capitals");
                intentAbout.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentAbout);

            }
        };
        buttonLogin.setOnClickListener(onClickListener);
        buttonRegister.setOnClickListener(onClickListener);
        buttonInfo.setOnClickListener(onClickListener);
        buttonAbout.setOnClickListener(onClickListener);

        userName = loadDataUser();
        //System.out.println("userName not log " + userName);

    }


    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }


}