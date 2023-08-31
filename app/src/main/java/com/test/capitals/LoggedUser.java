package com.test.capitals;

import static com.test.capitals.MainActivity.BEST_RESULT;
import static com.test.capitals.MainActivity.BEST_SCORE;
import static com.test.capitals.MainActivity.CORE_STATE;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.LAST_RESULT;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.POST_USER_SCORE;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
        try {
            postGetUserScore(userName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        TextView textView = findViewById(R.id.username);
        String text = "Hello, " + userName + "!";
        textView.setText(text);


        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.start_test) {
                Intent intentStartTest = new Intent(this, StartTest.class);
                intentStartTest.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentStartTest);
            } else if (v.getId() == R.id.info) {
                Intent intentInfo = new Intent(this, Info.class);
                intentInfo.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentInfo);
            } else if (v.getId() == R.id.about) {
                Intent intentAbout = new Intent(this, About.class);
                //intentAbout.putExtra(EXTRAS_COUNTY_LIST, countryList);
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

    private void postGetUserScore(String userName) throws JSONException {
        StringBuilder response = new StringBuilder();
        String jsonInputString = "{\"username\" : \"" + userName + "\"}";
        //Log.i("caps",  "postGetUserScore jsonInputString : " + jsonInputString);
        URL url;
        try {
            url = new URL(POST_USER_SCORE);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);

        }
        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader br = new BufferedReader(

                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String parsedResp = response.toString();
        parsedResp = parsedResp.substring(parsedResp.indexOf(":") + 1, parsedResp.length() - 1);

        Gson g = new Gson();
        UserScore userScore = g.fromJson(parsedResp, UserScore.class);
        updateUserScore(userScore.LAST_RES, userScore.BEST_RES, userScore.BESTSCORE);
    }

    public void updateUserScore(String lastRes, String bestRes, int bestScore) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_RESULT, lastRes);
        editor.putString(BEST_RESULT, bestRes);
        editor.putInt(BEST_SCORE, bestScore);
        editor.apply();
        Toast.makeText(this, "User data updated", Toast.LENGTH_SHORT).show();
    }


//    public ArrayList<UserAnswer> loadTestState(ArrayList<CountryDescribe> countryList) {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        String cS = sharedPreferences.getString(LAST_RESULT, "");
//        return ScoreMain.parseStringToUserAnswerList(cS, countryList);
//    }
}
