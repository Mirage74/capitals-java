package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.facebook.appevents.AppEventsLogger;
import java.util.ArrayList;



public class NotLoggedUser extends AppCompatActivity {
    String userName;
    Button buttonLogin, buttonRegister, buttonRules, buttonAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_logged_user);
        buttonLogin = findViewById(R.id.login);
        buttonRegister = findViewById(R.id.register);
        buttonRules = findViewById(R.id.testRules);
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
                Intent intentLogin = new Intent(this, LoginUser.class);
                intentLogin.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentLogin);
            } else if (v.getId() == R.id.register) {
                Intent intentReg = new Intent(this, RegisterUser.class);
                intentReg.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentReg);
            } else if (v.getId() == R.id.testRules) {
                Intent intentRules = new Intent(this, Rules.class);
                intentRules.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentRules);
            } else if (v.getId() == R.id.about) {
                Intent intentAbout = new Intent(this, About.class);
                startActivity(intentAbout);

            }
        };
        buttonLogin.setOnClickListener(onClickListener);
        buttonRegister.setOnClickListener(onClickListener);
        buttonRules.setOnClickListener(onClickListener);
        buttonAbout.setOnClickListener(onClickListener);
        userName = loadDataUser();
    }

    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }


}