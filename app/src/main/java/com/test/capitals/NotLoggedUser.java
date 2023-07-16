package com.test.capitals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class NotLoggedUser extends AppCompatActivity {
    public static final String EXTRAS_COUNTY_LIST = "country-list";
    Button buttonLogin, buttonRegister, buttonInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_logged_user);
        buttonLogin = findViewById(R.id.login);
        buttonRegister = findViewById(R.id.register);
        buttonInfo = findViewById(R.id.info);

        Intent intent = getIntent();
        List<countryDescribe> countryList = new ArrayList<>();
        countryList = (List<countryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTY_LIST );
        System.out.println("countryList : " + countryList);

        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.login) {
                //login();
            } else if (v.getId() == R.id.register) {
//                Intent intent ;
//                intent = new Intent("android.intent.action.express");
//                intent.putExtra("al", countryList);
//                startActivity(intent);
            } else if (v.getId() == R.id.info) {

            }
        };
        buttonLogin.setOnClickListener(onClickListener);
        buttonRegister.setOnClickListener(onClickListener);
        buttonInfo.setOnClickListener(onClickListener);

    }
}