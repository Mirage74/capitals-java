package com.test.capitals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

//import com.google.gson.FieldNamingStrategy;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefds";
    public static final String USER_NAME = "userName";
    public static final String LAST_RESULT = "last-result";
    public static final String BEST_RESULT = "best-result";
    public static final String BEST_SCORE = "best-score";
    public static final String NOT_LOGGED_USER = "notLoggedUser";
    public static final String EXTRAS_COUNTY_LIST = "country-list";
    public static final String EXTRAS_COUNTRY_CURRENT = "country-current";
    public static final String EXTRAS_DIFFICULT_LVL = "diff-lvl";
    //public static final String BACKEND_URL = "http://10.0.2.2:4000";
    public static final String BACKEND_URL = "http://192.168.1.14:4000";
    public static final String BACKEND_API = BACKEND_URL + "/api";
    public static final String POST_USER_SCORE = BACKEND_API + "/userScore";
    public static final String BEST_SCORE_FIELD_NAME_DB = "BESTSCORE";
    public static final String LASTRES_FIELD_NAME_DB = "LAST_RES";
    public static final String USER_ANSWER = "user-answer";
    public static final String CORE_STATE = "coreState";
    public static final String CORE_CURR_SCORE = "coreCurrScore";
    public static final String CORE_NEW = "coreNew";
    public static final String CORE_LAST_QUESTION_ID = "questionID";
    public static final String CORE_SEC_REST = "secRest";
    public static final String CORE_AV1 = "coreAV1";
    public static final String CORE_AV2 = "coreAV2";
    public static final String CORE_AV3 = "coreAV3";
    public static final String CORE_AV4 = "coreAV4";
    public static final String CORE_RA = "coreRA";


    String userName;

    //private static final String GET_URL = "http://10.0.2.2:4000/api/get";


    public String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }

    private String getCountryList() {
        String lineRes = "";
        URL url;
        try {
            url = new URL(BACKEND_API + "/get");
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
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        try {
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Invalid response from server: " + responseCode);
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                lineRes += line;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int i = lineRes.indexOf("[");
        int j = lineRes.indexOf("]");
        lineRes = lineRes.substring(i, j + 1);
        return lineRes;
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //printHashKey();


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        String lineRes = getCountryList();
        ArrayList<CountryDescribe> countryList = new ArrayList<>();


        Gson g = new Gson();
        Type userListType = new TypeToken<ArrayList<CountryDescribe>>() {
        }.getType();

        countryList = g.fromJson(lineRes, userListType);
        Intent intent;
        //saveUser(NOT_LOGGED_USER);
        userName = loadDataUser();
        if (userName != null && userName.equals(NOT_LOGGED_USER)) {
            intent = new Intent(this, NotLoggedUser.class);
            intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
            startActivity(intent);
        } else if ((userName != null) && (!userName.equals(NOT_LOGGED_USER))) {
            intent = new Intent(this, LoggedUser.class);
            intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error userName", Toast.LENGTH_SHORT).show();
        }
    }

//    public void printHashKey()
//    {
//        // Add code to print out the key hash
//        try {
//            PackageInfo info
//                    = getPackageManager().getPackageInfo(
//                    "com.test.capitals",
//                    PackageManager.GET_SIGNATURES);
//
//            for (Signature signature : info.signatures) {
//
//                MessageDigest md
//                        = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:",
//                        Base64.encodeToString(
//                                md.digest(),
//                                Base64.DEFAULT));
//            }
//        }
//
//        catch (PackageManager.NameNotFoundException e) {
//        }
//
//        catch (NoSuchAlgorithmException e) {
//        }
//    }





}



class CountryDescribe implements Serializable {

    int id;
    String countryName;
    String capitalName;
    int diffLvl;
    String imageName;


//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setCountryName(String countryName) {
//        this.countryName = countryName;
//    }
//
//    public void setCapitalName(String capitalName) {
//        this.capitalName = capitalName;
//    }
//
//    public void setDiffLvl(int diffLvl) {
//        this.diffLvl = diffLvl;
//    }
//
//    public void setImageName(String imageName) {
//        this.imageName = imageName;
//    }

    public CountryDescribe(int id, String countryName, String capitalName, int diffLvl, String imageName) {
        this.id = id;
        this.countryName = countryName;
        this.capitalName = capitalName;
        this.diffLvl = diffLvl;
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "countryDescribe{" +
                "id=" + id +
                ", countryName='" + countryName + '\'' +
                ", capitalName='" + capitalName + '\'' +
                ", diffLvl=" + diffLvl +
                ", imageName='" + imageName + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

}

class UserScore {
    String DISPLAYNAME;
    String LAST_RES;
    String BEST_RES;
    int BESTSCORE;

    @Override
    public String toString() {
        return "UserScore{" +
                "DISPLAYNAME='" + DISPLAYNAME + '\'' +
                ", LAST_RES='" + LAST_RES + '\'' +
                ", BEST_RES='" + BEST_RES + '\'' +
                ", BESTSCORE=" + BESTSCORE +
                '}';
    }

    public UserScore(String DISPLAYNAME, String LAST_RES, String BEST_RES, int BESTSCORE) {
        this.DISPLAYNAME = DISPLAYNAME;
        this.LAST_RES = LAST_RES;
        this.BEST_RES = BEST_RES;
        this.BESTSCORE = BESTSCORE;
    }
}
