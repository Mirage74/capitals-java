package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT;
import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT_PCT_EASY;
import static com.test.capitals.MainActivity.EXTRAS_USER_TEST_CURRENT_INFO;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CoreTest extends AppCompatActivity {
    ArrayList<CountryDescribe> countryListCut;
    ArrayList<Object> testState;
    ImageView imageView;
    private static final String GET_IMAGE_URL = "http://10.0.2.2:4000/test-image.jpg";

    int pctEasy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core_test);
        imageView = findViewById(R.id.poster);
        Intent intent = getIntent();
        pctEasy = intent.getIntExtra(EXTRAS_COUNTRY_CURRENT_PCT_EASY, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryListCut = intent.getParcelableArrayListExtra(EXTRAS_COUNTRY_CURRENT, CountryDescribe.class);
            testState = intent.getParcelableArrayListExtra(EXTRAS_USER_TEST_CURRENT_INFO, Object.class);

        } else {
            countryListCut = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTRY_CURRENT);
            testState = (ArrayList<Object>)intent.getSerializableExtra(EXTRAS_USER_TEST_CURRENT_INFO);
        }
//        URL url;
//        try {
//            url = new URL(GET_IMAGE_URL);
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//
//        }
        Picasso.get().load(GET_IMAGE_URL).into(imageView);

        //"http://10.0.2.2:4000/test-image.jpg";

        System.out.println("countryListCut : " + countryListCut);
        System.out.println("pctEasy : " + pctEasy);
        System.out.println("testState : " + testState);
    }

}

