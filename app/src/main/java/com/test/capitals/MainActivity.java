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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "shared_prefds";
    public static final String USER_NAME = "userName";
    public static final String NOT_LOGGED_USER = "notLoggedUser";
    public static final String EXTRAS_COUNTY_LIST = "country-list";

    String userName;

    private static final String GET_URL = "http://10.0.2.2:4000/api/get";


    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }

    private String getCountryList() {
        String lineRes = "";
        URL url;
        try {
            url = new URL(GET_URL);
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

        lineRes = cutGetStr(lineRes);
        System.out.println("lineRes : " + lineRes);
        return lineRes;
    }

    private String cutGetStr(String sIn) {
        String s = "";
        int i = sIn.indexOf("[");
        int j = sIn.indexOf("]");
        s = sIn.substring(i, j);
        return s;
    }

    private void parseString(List<countryDescribe> countryList, String sIn) {
        boolean isFinished = false;
        int cnt = 0, i, j;
        String s, sTemp, cntName, capName, imgName;
        int id, diffLvl;
        countryDescribe cD;
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
                cntName = s.substring(i, j - 1);

                s = s.substring(j + 1);
                i = s.indexOf("capitalName");
                i += 14;
                j = s.indexOf(",");
                capName = s.substring(i, j - 1);

                s = s.substring(j + 1);
                i = s.indexOf("diffLvl");
                i += 9;
                j = s.indexOf(",");
                diffLvl = Integer.parseInt(s.substring(i, j));

                s = s.substring(j + 1);
                i = s.indexOf("imageName");
                i += 12;
                j = s.indexOf(".jpg") + 4;
                imgName = s.substring(i, j);


                cD = new countryDescribe(id, cntName, capName, diffLvl, imgName);
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
        ArrayList<countryDescribe> countryList = new ArrayList<>();
        parseString(countryList, lineRes);
        System.out.println("countryList.get(3) : " + countryList.get(3));

        Intent intent;
        userName = loadDataUser();
        System.out.println("userName : " + userName);
        if (userName != null && userName.equals(NOT_LOGGED_USER)) {
            intent = new Intent("android.intent.action.not-logged-user");
            intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
            startActivity(intent);
        } else if (userName != null && !userName.equals(NOT_LOGGED_USER)) {
            intent = new Intent("android.intent.action.logged-user");
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error userName", Toast.LENGTH_SHORT).show();
        }
    }

}

class countryDescribe implements Serializable {

    int id;

    public countryDescribe() {
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

    public countryDescribe(int id, String countryName, String capitalName, int diffLvl, String imageName) {
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

    String capitalName;
    int diffLvl;
    String imageName;


}
