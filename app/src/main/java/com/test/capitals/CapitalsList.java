package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.USER_ANSWER;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class CapitalsList extends AppCompatActivity {
    ScrollView scrollView;
    TableLayout tableLayout;
    ArrayList<CountryDescribe> countryList;
    TextView tvID, tvCountryName, tvCapitalName;
    int smallTextSize;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capitals_list);
        tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        scrollView = findViewById(R.id.capList);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>)intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }
        showCountryList();
    }

    private void showCountryList() {
        String s;
        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;

        TableLayout.LayoutParams trParams = new
                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                bottomRowMargin);


        tvID = new TextView(this);
        tvID.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tvID.setGravity(Gravity.RIGHT);
        tvID.setPadding(1, 15, 0, 15);
        s = "No";
        tvID.setText(s);
        tvID.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

        tvCountryName = new TextView(this);
        tvCountryName.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tvCountryName.setGravity(Gravity.LEFT);
        tvCountryName.setPadding(25, 15, 0, 15);
        s = "Country";
        tvCountryName.setText(s);
        tvCountryName.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

        tvCapitalName = new TextView(this);
        tvCapitalName.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tvCapitalName.setGravity(Gravity.LEFT);
        tvCapitalName.setPadding(5, 15, 0, 15);
        s = "Capital";
        tvCapitalName.setText(s);
        tvCapitalName.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

        final TableRow trRowsNames = new TableRow(this);
        trRowsNames.setId(0);

        trRowsNames.setPadding(0,0,0,1);
        trRowsNames.setLayoutParams(trParams);
        trRowsNames.addView(tvID);
        trRowsNames.addView(tvCountryName);
        trRowsNames.addView(tvCapitalName);
        tableLayout.addView(trRowsNames);


        for (int k = 0; k < countryList.size(); k++) {
            int i;
            int finalK = k;

            tvID = new TextView(this);
            tvID.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvID.setGravity(Gravity.RIGHT);
            tvID.setPadding(1, 15, 0, 15);
            i = k + 1;
            s = "" + i;
            tvID.setText(s);
            tvID.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

            tvCountryName = new TextView(this);
            tvCountryName.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCountryName.setGravity(Gravity.LEFT);
            tvCountryName.setPadding(25, 15, 0, 15);
            s = "" + countryList.get(k).countryName;
            tvCountryName.setText(s);
            tvCountryName.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

            tvCapitalName = new TextView(this);
            tvCapitalName.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCapitalName.setGravity(Gravity.LEFT);
            tvCapitalName.setPadding(5, 15, 0, 15);
            s = "" + countryList.get(k).capitalName;
            tvCapitalName.setText(s);
            tvCapitalName.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

            final TableRow tr = new TableRow(this);
            tr.setId(k + 1);

            tr.setPadding(0,0,0,1);
            tr.setLayoutParams(trParams);
            tr.addView(tvID);
            tr.addView(tvCountryName);
            tr.addView(tvCapitalName);
            tableLayout.addView(tr);
        }
        scrollView.addView(tableLayout);
    }

}
