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

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;


public class NotLoggedUser extends AppCompatActivity {
    private Button mButtonFacebook;

    private CallbackManager callbackManager;
    private LoginManager loginManager;
    String userName;
    Button buttonLogin, buttonRegister, buttonRules, buttonAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_logged_user);

        printHashKey();

        mButtonFacebook
                = findViewById(R.id.button_facebook);
        //FacebookSdk.sdkInitialize(NotLoggedUser.this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin();

        mButtonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginManager.logInWithReadPermissions(
                        NotLoggedUser.this,
                        Arrays.asList(
                                "email",
                                "public_profile",
                                "user_birthday"));
            }
        });


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
        //System.out.println("userName not log " + userName);

    }

    public void facebookLogin()
    {

        loginManager
                = LoginManager.getInstance();
        callbackManager
                = CallbackManager.Factory.create();

        loginManager
                .registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult)
                            {
                                GraphRequest request = GraphRequest.newMeRequest(

                                        loginResult.getAccessToken(),

                                        new GraphRequest.GraphJSONObjectCallback() {

                                            @Override
                                            public void onCompleted(JSONObject object,
                                                                    GraphResponse response)
                                            {

                                                if (object != null) {
                                                    try {
                                                        String name = object.getString("name");
                                                        String email = object.getString("email");
                                                        String fbUserID = object.getString("id");

                                                        disconnectFromFacebook();

                                                        // do action after Facebook login success
                                                        // or call your API
                                                    }
                                                    catch (JSONException | NullPointerException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString(
                                        "fields",
                                        "id, name, email, gender, birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel()
                            {
                                Log.v("LoginScreen", "---onCancel");
                            }

                            @Override
                            public void onError(FacebookException error)
                            {
                                // here write code when get error
                                Log.v("LoginScreen", "----onError: "
                                        + error.getMessage());
                            }
                        });
    }


    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse)
                    {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        // add this line
        callbackManager.onActivityResult(
                requestCode,
                resultCode,
                data);

        super.onActivityResult(requestCode,
                resultCode,
                data);
    }

        public void printHashKey()
    {
        // Add code to print out the key hash
        try {
            PackageInfo info
                    = getPackageManager().getPackageInfo(
                    "com.test.capitals",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md
                        = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:",
                        Base64.encodeToString(
                                md.digest(),
                                Base64.DEFAULT));
            }
        }

        catch (PackageManager.NameNotFoundException e) {
        }

        catch (NoSuchAlgorithmException e) {
        }
    }


    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }


}