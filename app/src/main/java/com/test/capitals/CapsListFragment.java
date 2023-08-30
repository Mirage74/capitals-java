package com.test.capitals;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
        //tableLayout.setWeightSum(11);
        createFragmentTable(capsUN.countryListFiltered);

    }

    void createFragmentTable(ArrayList<CountryDescribe> countryListFiltered ) {
        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        int beginSubst;
        CountryDescribe currRecord;

        TableLayout.LayoutParams trParams = new
                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                bottomRowMargin);

        for (int k = 0; k < countryListFiltered.size(); k++) {
            currRecord = countryListFiltered.get(k);

            tvID = new TextView(capsUN);
            tvID.setLayoutParams(new
                    TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 1));
//            Log.i("alc",  "CapListFrag k " + k);
//            Log.i("alc",  "CapListFrag width 1 " + tvID.getWidth());
            tvID.setGravity(Gravity.RIGHT);
            tvID.setPadding(3, 15, 0, 15);


//Z, T
            tvID.setTextSize(TypedValue.COMPLEX_UNIT_PX, capsUN.defTextSize - 8);
            //tvID.setMaxWidth(capsUN.rawIdWidth);
            s = "" + currRecord.id + "   ";
            tvID.setText(s);
            //tvID.setBackground(capsUN.drawable);
            //Log.i("alc",  "CapListFrag width 2 " + tvID.getWidth());

            tvCountryName = new TextView(capsUN);
            tvCountryName.setLayoutParams(new
                    TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 4));
            tvCountryName.setGravity(Gravity.LEFT);
            tvCountryName.setPadding(5, 15, 0, 15);
            s = "" + currRecord.countryName;
            beginSubst = s.toLowerCase().indexOf(capsUN.sFilter.toLowerCase());
            if (capsUN.currModeByCountry && capsUN.sFilter.length() > 1 && beginSubst >= 0) {
                SpannableStringBuilder str = new SpannableStringBuilder(s);

                str.setSpan(new ForegroundColorSpan(Color.GREEN), beginSubst, beginSubst + capsUN.sFilter.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvCountryName.setText(str);
            } else {
                tvCountryName.setText(s);
            }

            tvCountryName.setTextSize(TypedValue.COMPLEX_UNIT_PX, capsUN.defTextSize);
            //tvCountryName.setBackground(capsUN.drawable);


            tvCapitalName = new TextView(capsUN);
            tvCapitalName.setLayoutParams(new
                    TableRow.LayoutParams(0,
                    TableRow.LayoutParams.WRAP_CONTENT, 4));
            tvCapitalName.setGravity(Gravity.LEFT);
            tvCapitalName.setPadding(5, 15, 0, 15);
            s = "" + currRecord.capitalName;
            beginSubst = s.toLowerCase().indexOf(capsUN.sFilter.toLowerCase());
            if (!capsUN.currModeByCountry && capsUN.sFilter.length() > 1 && beginSubst >= 0) {
                SpannableStringBuilder str = new SpannableStringBuilder(s);
                str.setSpan(new ForegroundColorSpan(Color.GREEN), beginSubst, beginSubst + capsUN.sFilter.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvCapitalName.setText(str);
            } else {
                tvCapitalName.setText(s);
            }

            tvCapitalName.setTextSize(TypedValue.COMPLEX_UNIT_PX, capsUN.defTextSize);
            //tvCapitalName.setBackground(capsUN.drawable);


            final TableRow tr = new TableRow(capsUN);

            //tr.setPadding(0,3,0,3);
            tr.setId(k + 1);

            //tr.setPadding(0,0,0,1);
            tr.setLayoutParams(trParams);
            tr.setGravity(Gravity.CENTER);
            tr.setWeightSum(9);
            tr.addView(tvID);
            tr.addView(tvCountryName);
            tr.addView(tvCapitalName);
            tr.setBackground(capsUN.drawable);
            tableLayout.addView(tr);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.addView(tableLayout);
        return inflater.inflate(R.layout.frag_caps_un, container, false);
    }
}