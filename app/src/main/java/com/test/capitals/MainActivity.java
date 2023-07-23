package com.test.capitals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefds";
    public static final String USER_NAME = "userName";
    public static final String NOT_LOGGED_USER = "notLoggedUser";
    public static final String EXTRAS_COUNTY_LIST = "country-list";
    public static final String EXTRAS_COUNTRY_CURRENT = "country-current";
    public static final String EXTRAS_COUNTRY_CURRENT_PCT_EASY = "pct-easy";
    public static final String EXTRAS_USER_TEST_CURRENT_INFO = "test-core-info";
    public static final String EXTRAS_DIFFICULT_LVL = "diff-lvl";

    public static final String BACKEND_URL = "http://10.0.2.2:4000";
    public static final String BACKEND_API = BACKEND_URL + "/api";


    String userName;

    //private static final String GET_URL = "http://10.0.2.2:4000/api/get";


    private String loadDataUser() {
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
        //System.out.println("lineRes 111: " + lineRes);
        lineRes = cutGetStr(lineRes);
        //System.out.println("lineRes 59: " + lineRes);
        return lineRes;
    }

    private String cutGetStr(String sIn) {
        String s = "";
        int i = sIn.indexOf("[");
        int j = sIn.indexOf("]");
        s = sIn.substring(i, j);
        return s;
    }

    private void parseString(List<CountryDescribe> countryList, String sIn) {
        boolean isFinished = false;
        int cnt = 0, i, j;
        String s, cntName, capName, imgName;
        int id, diffLvl;
        CountryDescribe cD;
        s = sIn;
        while (!isFinished) {
            i = s.indexOf("{");
            if (i > -1) {
                i += 6;
                j = s.indexOf(",");
                id = Integer.parseInt(s.substring(i, j));

                s = s.substring(j + 1);
                i = s.indexOf("countryName");
                i += 14;
                j = s.indexOf(",");
                cntName = s.substring(i, j - 1).trim();

                s = s.substring(j + 1);
                i = s.indexOf("capitalName");
                i += 14;
                j = s.indexOf(",");
                capName = s.substring(i, j - 1).trim();

                s = s.substring(j + 1);
                i = s.indexOf("diffLvl");
                i += 9;
                j = s.indexOf(",");
                diffLvl = Integer.parseInt(s.substring(i, j));

                s = s.substring(j + 1);
                i = s.indexOf("imageName");
                i += 12;
                j = s.indexOf(".jpg") + 4;
                imgName = s.substring(i, j).trim();


                cD = new CountryDescribe(id, cntName, capName, diffLvl, imgName);
                countryList.add(cD);
//                }
                i = s.indexOf("{");
                if (i > 10) {
                    s = s.substring(i);
                }

                if (i < 0 || s.length() < 10) {
                    isFinished = true;
                }

            } else {
                isFinished = true;
            }


        }
        //System.out.println("country list 59 " + countryList.get(59));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }
        String lineRes = getCountryList();
        ArrayList<CountryDescribe> countryList = new ArrayList<>();
        parseString(countryList, lineRes);
        //System.out.println("countryList.get(3) : " + countryList.get(3));

        Intent intent;
        //saveUser(NOT_LOGGED_USER);
        userName = loadDataUser();

//        System.out.println("userName : " + userName.getClass());
//        System.out.println("NOT_LOGGED_USER : " + NOT_LOGGED_USER.getClass());
//        if (!userName.equals(NOT_LOGGED_USER)) {
//            System.out.println("not equal");
//        }
        if (userName != null && userName.equals(NOT_LOGGED_USER)) {
            intent = new Intent("android.intent.action.not-logged-user");
            intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
            startActivity(intent);

        } else if ( (userName != null) && (!userName.equals(NOT_LOGGED_USER)))  {
            intent = new Intent("android.intent.action.logged-user-capitals");
            intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error userName", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveUser(String userName) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
        Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
    }

}

class CountryDescribe implements Serializable {

    int id;
    String capitalName;
    int diffLvl;
    String imageName;

    public CountryDescribe() {
    }

    String countryName;

    public void setId(int id) {
        this.id = id;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCapitalName(String capitalName) {
        this.capitalName = capitalName;
    }

    public void setDiffLvl(int diffLvl) {
        this.diffLvl = diffLvl;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

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

    public String getCountryName() {
        return countryName;
    }

    public String getCapitalName() {
        return capitalName;
    }

    public int getDiffLvl() {
        return diffLvl;
    }

    public String getImageName() {
        return imageName;
    }




}
