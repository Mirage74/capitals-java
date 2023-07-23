package com.test.capitals;

import static com.test.capitals.MainActivity.BACKEND_API;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
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

public class RegisterUser extends AppCompatActivity {
    private static final String POST_CREATE_USER = BACKEND_API + "/createUser";
    private static final String POST_BEGIN_RESPONSE = "CODE REG_USER_01";
    String userName;
    EditText name, pass;
    Button buttonRegister, buttonBack;
    String name1, pass1;
    ImageView eye;
    boolean state = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Intent intent = getIntent();
        final ArrayList<CountryDescribe> countryList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }

        name = findViewById(R.id.user2);
        pass = findViewById(R.id.pass2);
        buttonRegister = findViewById(R.id.register);
        buttonBack = findViewById(R.id.back);


        eye = findViewById(R.id.toggle_view2);
        name1 = "";
        pass1 = "";

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }



        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.register) {
                register(countryList);
            } else if (v.getId() == R.id.toggle_view2) {
                toggle();
            } else if (v.getId() == R.id.back) {
                Intent intentBack = new Intent("android.intent.action.not-logged-user");
                intentBack.putExtra(EXTRAS_COUNTY_LIST, countryList);
                startActivity(intentBack);
            }

        };
        buttonRegister.setOnClickListener(onClickListener);
        buttonBack.setOnClickListener(onClickListener);
        eye.setOnClickListener(onClickListener);
    }

    private boolean checkUsername(String s) {
        boolean flag = true;
        if (s.contains("\"")) {
            flag = false;
        }
        return flag;
    }
    public void register(ArrayList<CountryDescribe> countryList) {
        name1 = name.getText().toString();
        pass1 = pass.getText().toString();


        if (name1.isEmpty() || pass1.isEmpty()) {
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_SHORT).show();  // Check whether the fields are not blank
        } else {
            if (!checkUsername(name1)) {
                Toast.makeText(this, "Fields USERNAME contain illegal symbol(s)", Toast.LENGTH_SHORT).show();
            } else {
                // Create various messages to display in the app.
                Toast failed_registration = Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT);
                Toast success_registration = Toast.makeText(this, "Register success", Toast.LENGTH_SHORT);
                String getRegisterResponse = postRegisterUser(name1, pass1);
                String user = getUserNameFromBackendResponse(getRegisterResponse);
                if (user.equals(getRegisterResponse)) {
                    System.out.println("User registration failed : " + user);
                    failed_registration.show();
                } else {
                    System.out.println("user register : " + user);
                    success_registration.show();
                    saveUser(user);
                    Intent intent = new Intent("android.intent.action.logged-user-capitals");
                    intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
                    System.out.println("login user, countryList : " + countryList);
                    startActivity(intent);
                }
            }
        }
    }

    private String postRegisterUser(String login, String password) {
        StringBuilder response = new StringBuilder();
        String jsonInputString = "{\"login\" : \"" + login + "\"" + ", \"password\" : \"" + password + "\"}";
        URL url;
        try {
            url = new URL(POST_CREATE_USER);
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
        if (sIn.indexOf(POST_BEGIN_RESPONSE) != 1) {
            return (sIn);
        } else {
            int i = sIn.indexOf("\"", 10);
            int j = sIn.indexOf("\"", i + 1);
            s = sIn.substring(i + 1, j - 1);
            s = s.substring(0, 1).toUpperCase() + s.substring(1);
            System.out.println("s : " + s);
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

    public void saveUser(String userName) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
        Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
    }
}
