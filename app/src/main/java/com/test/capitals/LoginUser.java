package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class LoginUser extends AppCompatActivity {
    private static final String POST_LOGIN_USER = "http://10.0.2.2:4000/api/login";
    String userName;
    EditText name, pass;
    Button buttonLogin, buttonBack;
    String name1, pass1;
    ImageView eye;
    boolean state = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Intent intent = getIntent();
        final ArrayList<CountryDescribe> countryList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }
        //System.out.println("login user, countryList 222: " + countryList);

        name = findViewById(R.id.user1);
        pass = findViewById(R.id.pass1);
        buttonLogin = findViewById(R.id.login);
        buttonBack = findViewById(R.id.back);


        eye = findViewById(R.id.toggle_view1);
        name1 = "";
        pass1 = "";

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        userName = loadDataUser();
        System.out.println("userName before : " + userName);

        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.login) {
                login(countryList);
            } else if (v.getId() == R.id.toggle_view1) {
                toggle();
            } else if (v.getId() == R.id.back) {
                Intent intentBack = new Intent("android.intent.action.not-logged-user");
                intentBack.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentBack);
            }

        };
        buttonLogin.setOnClickListener(onClickListener);
        buttonBack.setOnClickListener(onClickListener);
        eye.setOnClickListener(onClickListener);
    }

    public void login(ArrayList<CountryDescribe> countryList) {
        name1 = name.getText().toString();
        pass1 = pass.getText().toString();
        //email1 = email.getText().toString();

        if (name1.isEmpty() || pass1.isEmpty()) {
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();  // Check whether the fields are not blank
        } else {
            // Create various messages to display in the app.
            Toast failed_login = Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT);
            Toast success_login = Toast.makeText(this, "Login success", Toast.LENGTH_SHORT);
            String getLoginResponse = postLoginUser(name1, pass1);
            String user = getUserNameFromBackendResponse(getLoginResponse);
            if (user.equals(getLoginResponse)) {
                System.out.println("User login failed : " + user);
                failed_login.show();
            } else {
                System.out.println("user : " + user);
                success_login.show();
                saveUser(user);
                Intent intent = new Intent("android.intent.action.logged-user-capitals");
                intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
                System.out.println("login user, countryList : " + countryList);
                startActivity(intent);
            }

        }
    }

    private String postLoginUser(String login, String password) {
        StringBuilder response = new StringBuilder();
        String jsonInputString = "{\"login\" : \"" + login + "\"" + ", \"password\" : \"" + password + "\"}";
        //System.out.println("jsonInputString : " + jsonInputString);
        URL url;
        try {
            url = new URL(POST_LOGIN_USER);
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
        return response.toString();
    }

    private String getUserNameFromBackendResponse(String sIn) {
        String s = "";
        if (sIn.indexOf("CODE LOGIN_USER_02") == 1) {
            return (sIn);
        } else {

            int i = sIn.indexOf(":");
            int j = sIn.indexOf("}");
            s = sIn.substring(i + 2, j - 1);
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            return s;
        }
    }

    public void toggle() {
        if (!state) {
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pass.setSelection(pass.getText().length());
            eye.setImageResource(R.drawable.eye);
        } else {
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pass.setSelection(pass.getText().length());
            eye.setImageResource(R.drawable.eye_off);
        }
        state = !state;
    }

    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }

    public void saveUser(String userName) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
        Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
    }
}
