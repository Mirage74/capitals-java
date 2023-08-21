package com.test.capitals;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.USER_ANSWER;
import static com.test.capitals.ScoreMain.LAST_SCORE_MODE;
import static com.test.capitals.ScoreMain.parseStringToUserAnswerList;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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


public class LastResFragment extends Fragment {
    public LastResFragment() {
        // Required empty public constructor
    }
    ScoreMain scoreMain;
    TableLayout tableLayout;
    String s;
    TextView tvQuizNum, tvCountryName, tvScorePoints;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scoreMain = (ScoreMain)getActivity();
        tableLayout = new TableLayout(scoreMain);
        tableLayout.setStretchAllColumns(true);
        //Log.i("alc",  "LastResFragment onCreate");

        //Log.i("alc",  "LastResFragment currScore " + scoreMain.currScore);
        if (scoreMain.currViewMode == LAST_SCORE_MODE) {
            createFragmentTablePortrait(parseStringToUserAnswerList(scoreMain.lastQuestResult, scoreMain.countryList));
        } else {
            createFragmentTablePortrait(parseStringToUserAnswerList(scoreMain.bestQuestResult, scoreMain.countryList));
        }


    }

    public void onResume(Bundle savedInstanceState) {
        super.onResume();
        Log.i("alc",  "LastResFragment onResume");


    }

    public void onStart(Bundle savedInstanceState) {
        super.onStart();
        Log.i("alc",  "LastResFragment onStart");
    }

    public void onPause(Bundle savedInstanceState) {
        super.onPause();
        Log.i("alc",  "LastResFragment onPause");
    }

    public void onStop(Bundle savedInstanceState) {
        super.onStop();
        Log.i("alc",  "LastResFragment onStop");
    }

    public void onDestroy(Bundle savedInstanceState) {
        Log.i("alc",  "LastResFragment onDestroy");
    }

    void createFragmentTablePortrait(ArrayList<UserAnswer> testState) {
//        Log.i("alc",  "frag currViewMode  : " + scoreMain.currViewMode);
//        Log.i("alc",  "frag testState  : " + testState);
        if ( (testState == null) || (testState.size() == 0) ) {
            scoreMain.params.height = getResources().getInteger(R.integer.layoutNullScoreMaxHeight);
            scoreMain.linearLayout.setLayoutParams(scoreMain.params);
        }


        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;

//        scoreMain.buttonSwitch.setVisibility(View.INVISIBLE);
//        scoreMain.buttonBack.setVisibility(View.INVISIBLE);

        TableLayout.LayoutParams trParams = new
                TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin,
                bottomRowMargin);

        View.OnClickListener onClickListener = v -> {
            Intent intent = new Intent(scoreMain, CheckWrongAnswer.class);
            UserAnswer ua = testState.get((int)v.getId() - 1);
            intent.putExtra(USER_ANSWER, ua);
            intent.putExtra(EXTRAS_COUNTY_LIST, scoreMain.countryList);
            startActivity(intent);
        };

        for (int k = 0; k < testState.size(); k++) {
            int finalK = k;
            scoreMain.userAnswer = testState.stream().filter(e -> e.answerNum == finalK + 1).findFirst().get();

            tvQuizNum = new TextView(scoreMain);
            tvQuizNum.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvQuizNum.setGravity(Gravity.RIGHT);
            tvQuizNum.setPadding(1, 15, 0, 15);
            s = "" + scoreMain.userAnswer.answerNum + "   ";
            tvQuizNum.setText(s);
            tvQuizNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, scoreMain.smallTextSize);

            tvCountryName = new TextView(scoreMain);
            tvCountryName.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCountryName.setGravity(Gravity.LEFT);
            tvCountryName.setPadding(5, 15, 0, 15);
            s = "" + scoreMain.userAnswer.countryName;
            tvCountryName.setText(s);
            tvCountryName.setTextSize(TypedValue.COMPLEX_UNIT_PX, scoreMain.smallTextSize);


            TextView tvCheck = new TextView(scoreMain);
            tvCheck.setId(k + 1);
            tvCheck.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvCheck.setGravity(Gravity.CENTER);

            tvCheck.setText(R.string.check);

            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius(getResources().getInteger(R.integer.buttonCheckCornerRadius));
            shape.setColor(ContextCompat.getColor(scoreMain.getApplicationContext(), R.color.buttonCheckBgColor));
            tvCheck.setBackground(shape);

            tvCheck.setTextSize(TypedValue.COMPLEX_UNIT_PX, scoreMain.smallTextSize);
            if (scoreMain.userAnswer.score > 0) {
                tvCheck.setVisibility(View.INVISIBLE);
            } else {
                tvCheck.setOnClickListener(onClickListener);
            }



            tvScorePoints = new TextView(scoreMain);
            tvScorePoints.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tvScorePoints.setGravity(Gravity.CENTER);
            tvScorePoints.setPadding(5, 15, 1, 15);
            s = "" + scoreMain.userAnswer.score;
            tvScorePoints.setText(s);
            if (scoreMain.userAnswer.score > 0) {
                tvScorePoints.setTextColor(Color.parseColor(scoreMain.colorRight));
            } else {
                tvScorePoints.setTextColor(Color.parseColor("" + scoreMain.colorWrong));
            }
            tvScorePoints.setTextSize(TypedValue.COMPLEX_UNIT_PX, scoreMain.smallTextSize);


            final TableRow tr = new TableRow(scoreMain);
            tr.setId(k + 1);

            tr.setPadding(0,0,0,1);
            tr.setLayoutParams(trParams);
            tr.addView(tvQuizNum);
            tr.addView(tvCountryName);
            tr.addView(tvCheck);
            tr.addView(tvScorePoints);
            tableLayout.addView(tr);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.i("alc",  "LastResFragment onCreateView");
        container.addView(tableLayout);
        scoreMain.buttonSwitch.setVisibility(View.VISIBLE);
        scoreMain.buttonBack.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_last_res, container, false);
    }


}





