package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.USER_ANSWER;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class CapsListFragment extends Fragment {

    public CapsListFragment() {}
    CapsUN capsUN;
    TableLayout tableLayout;
    String s;

    TextView tvID, tvCountryName, tvCapitalName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        capsUN = (CapsUN)getActivity();
        tableLayout = new TableLayout(capsUN);
        tableLayout.setStretchAllColumns(true);
        createFragmentTable(capsUN.countryListFiltered);
    }

    void createFragmentTable(ArrayList<CountryDescribe> countryListFiltered ) {
        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        CountryDescribe currRecord;

        TableLayout.LayoutParams trParams = new
                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                bottomRowMargin);

        for (int k = 0; k < countryListFiltered.size(); k++) {
            int finalK = k;
            currRecord = countryListFiltered.get(k);

            tvID = new TextView(capsUN);
            tvID.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvID.setGravity(Gravity.RIGHT);
            tvID.setPadding(1, 15, 0, 15);
            s = "" + currRecord.id + "   ";
            tvID.setText(s);
            tvID.setTextSize(TypedValue.COMPLEX_UNIT_PX, capsUN.smallTextSize);

            tvCountryName = new TextView(capsUN);
            tvCountryName.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCountryName.setGravity(Gravity.LEFT);
            tvCountryName.setPadding(5, 15, 0, 15);
            s = "" + currRecord.countryName;
            tvCountryName.setText(s);
            tvCountryName.setTextSize(TypedValue.COMPLEX_UNIT_PX, capsUN.smallTextSize);


            tvCapitalName = new TextView(capsUN);
            tvCapitalName.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCapitalName.setGravity(Gravity.LEFT);
            tvCapitalName.setPadding(5, 15, 0, 15);
            s = "" + currRecord.capitalName;
            tvCapitalName.setText(s);
            tvCapitalName.setTextSize(TypedValue.COMPLEX_UNIT_PX, capsUN.smallTextSize);

            final TableRow tr = new TableRow(capsUN);
            tr.setId(k + 1);

            tr.setPadding(0,0,0,1);
            tr.setLayoutParams(trParams);
            tr.addView(tvID);
            tr.addView(tvCountryName);
            tr.addView(tvCapitalName);
            tableLayout.addView(tr);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.addView(tableLayout);
        return inflater.inflate(R.layout.frag_caps_un, container, false);
    }
}