package com.test.capitals;

import static com.test.capitals.CoreTest.NO_ANSWER_TIME_EXPIRED;
import static com.test.capitals.MainActivity.BACKEND_API;
import static com.test.capitals.MainActivity.BACKEND_URL;
import static com.test.capitals.MainActivity.BEST_SCORE_FIELD_NAME_DB;
import static com.test.capitals.MainActivity.EXTRAS_COUNTRY_CURRENT;
import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;
import static com.test.capitals.MainActivity.EXTRAS_DIFFICULT_LVL;
import static com.test.capitals.MainActivity.LASTRES_FIELD_NAME_DB;
import static com.test.capitals.MainActivity.NOT_LOGGED_USER;
import static com.test.capitals.MainActivity.SHARED_PREFS;
import static com.test.capitals.MainActivity.USER_NAME;
import static com.test.capitals.MainActivity.LAST_RESULT;
import static com.test.capitals.MainActivity.BEST_SCORE;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;



public class CoreTest extends AppCompatActivity {
    private static final String POST_UPDATE_USER = BACKEND_API + "/updateUser";
    public static final String NO_ANSWER_TIME_EXPIRED = "NoAnswer";

    static boolean timeExpire = false;

    ArrayList<CountryDescribe> countryList, countryListCut, countryListExactDiffLevel, countryListTemp;
    ArrayList<UserAnswer> testState = new ArrayList<>();
    Button buttonCap1, buttonCap2, buttonCap3, buttonCap4;
    String userName;
    int bestScore;
    CountryDescribe testCountry;
    ImageView ivImageCapital;
    TextView textViewUsername, textViewCountryName, testViewQuestionNumber;

    public void setTextViewSecValue(String sec) {
        this.textViewSec.setText(sec);
    }

    TextView textViewSec;
    int allQuestions, scoreEasy, scoreMedium, scoreHard, diffLvl, sumScore = 0;
    int timeBestScoreSec, timeMediumScoreSec, timeMaxSec, currTime, rightAnswerNum;
    String[] answerVariants = new String[4];
    Random rand = new Random();
    Timer timer;


    private void procAnswer(int answer) {
        int score = 0;
        String rightAnswer = answerVariants[rightAnswerNum];
        String yourAnswer;
        if (answer == 0) {
            yourAnswer = NO_ANSWER_TIME_EXPIRED;
        } else {
            int secRest = Integer.parseInt((String) textViewSec.getText());
            if (answer == rightAnswerNum + 1) {
                if (secRest >= timeBestScoreSec) {
                    score = scoreHard;
                } else if (secRest >= timeMediumScoreSec) {
                    score = scoreMedium;
                } else {
                    score = scoreEasy;
                }
                yourAnswer = rightAnswer;
            } else {
                yourAnswer = answerVariants[answer - 1];
            }
        }
        sumScore += score;
        UserAnswer userAnswer = new UserAnswer( testCountry.id, testState.size() + 1, yourAnswer, score, countryList );
        testState.add(userAnswer);
        //Log.i("caps",  "N, userAnswer : " + userAnswer.serializeToString());
        if ( testState.size() < allQuestions) {
            runFrameQuest(testState.size() + 1);
        } else {
            String arrStr = "";
            for (int i = 0; i < testState.size(); i++) {
                arrStr = arrStr + testState.get(i).serializeToString();
            }

            postUpdateUserScore(arrStr, sumScore);

//            Log.i("caps",  "arrStr len : " + arrStr.length());
//            Log.i("caps",  "sumScore : " + sumScore);
//            Log.i("caps",  "resUpd : " + resUpd);

            //Log.i("caps",  "coretest loadLastResult before " + loadLastResult());
            updateUserScore(arrStr, sumScore);

            Intent intent = new Intent(this, ScoreMain.class);
            //Log.i("caps",  "coretest activity countryList put : " + countryList);
            intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
            startActivity(intent);

            //Log.i("caps",  "coretest loadLastResult after" + loadLastResult());


//            Log.i("caps",  "score : " + testState.stream().map(e -> e.score).mapToInt(Integer::intValue).sum());
//            Log.i("caps",  "countryList len finish : " + countryList.size());
//            Log.i("caps",  "countryListExactDiffLevel len finish  : " + countryListExactDiffLevel.size());
//            Log.i("caps",  "countryListCut len finish : " + countryListCut.size());

        }

    }

    @Override
    public void onStart(){
        super.onStart();
        //Log.i("alc",  "CoreTest onStart, countryList  : " + countryList);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        //Log.i("alc",  "CoreTest onRestart, countryList  : " + countryList);
    }

    @Override
    public void onResume(){
        super.onResume();
        //Log.i("alc",  "CoreTest onResume, countryList  : " + countryList);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.i("alc",  "CoreTest onPause, countryList  : " + countryList);
    }

    @Override
    public void onStop(){
        super.onStop();
        //Log.i("alc",  "CoreTest onStop, countryList  : " + countryList);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //Log.i("alc",  "CoreTest onDestroy, countryList  : " + countryList);
    }

    private void runFrameQuest(int questNum) {
        int int_random;
        timeExpire = false;
        if (questNum % 2 == 1) {
            int_random = rand.nextInt(countryListExactDiffLevel.size());
            testCountry = countryListExactDiffLevel.get(int_random);

            textViewCountryName.setTextSize(getResources().getInteger(R.integer.tvCountryTextSizeNormal));
            if (testCountry.countryName.length() > getResources().getInteger(R.integer.tvCountryTextChangeToSmallLength)) {
                if (testCountry.countryName.length() > getResources().getInteger(R.integer.tvCountryTextChangeToTinyLength)) {
                    textViewCountryName.setTextSize(getResources().getInteger(R.integer.tvCountryTextSizeTiny));
                } else {
                        textViewCountryName.setTextSize(getResources().getInteger(R.integer.tvCountryTextSizeSmall));
                }
            }


            textViewCountryName.setText(testCountry.countryName);
            String s = testState.size() + 1 + " / " + allQuestions;
            testViewQuestionNumber.setText(s);
            countryListExactDiffLevel.remove(int_random);
            ArrayList<CountryDescribe> cL = new ArrayList<>();
            cL = (ArrayList) countryListCut.stream().filter(e -> e.id != testCountry.id).collect(Collectors.toList());
            countryListCut = (ArrayList) cL.clone();
            countryListTemp = new ArrayList<>();
            countryListTemp = (ArrayList) countryList.clone();
        } else {
            int_random = rand.nextInt(countryListCut.size());
            testCountry = countryListCut.get(int_random);

            textViewCountryName.setTextSize(getResources().getInteger(R.integer.tvCountryTextSizeNormal));

            if (testCountry.countryName.length() > getResources().getInteger(R.integer.tvCountryTextChangeToSmallLength)) {
                if (testCountry.countryName.length() > getResources().getInteger(R.integer.tvCountryTextChangeToTinyLength)) {
                    textViewCountryName.setTextSize(getResources().getInteger(R.integer.tvCountryTextSizeTiny));
                } else {
                        textViewCountryName.setTextSize(getResources().getInteger(R.integer.tvCountryTextSizeSmall));
                }
            }


            textViewCountryName.setText(testCountry.countryName);
            String s = testState.size() + 1 + " / " + allQuestions;
            testViewQuestionNumber.setText(s);
            countryListCut.remove(int_random);
            ArrayList<CountryDescribe> cL = new ArrayList<>();
            cL = (ArrayList) countryListExactDiffLevel.stream().filter(e -> e.id != testCountry.id).collect(Collectors.toList());
            countryListExactDiffLevel = (ArrayList) cL.clone();
            countryListTemp = new ArrayList<>();
            countryListTemp = (ArrayList) countryList.clone();
        }

        rightAnswerNum = rand.nextInt(3);
        for (int i = 0; i < 4; i++) {
            answerVariants[i] = null;
        }
        answerVariants[rightAnswerNum] = testCountry.capitalName;
        System.out.println("rightAnswerNum : " + rightAnswerNum);

        countryListTemp = (ArrayList) countryList.stream().filter(e -> e.id != testCountry.id).collect(Collectors.toList());
        FillAnswer();
        FillAnswer();
        FillAnswer();

        fitTextButton();
        buttonCap1.setText(answerVariants[0]);
        buttonCap2.setText(answerVariants[1]);
        buttonCap3.setText(answerVariants[2]);
        buttonCap4.setText(answerVariants[3]);
        Picasso.get().load(BACKEND_URL + "/" + testCountry.imageName).into(ivImageCapital);
        currTime = timeMaxSec;
        timer = new Timer();
        TimerTask timerTask = new TimerTaskSecAnswer();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);

    }

    private void fitTextButton() {
        buttonCap1.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeNormal));
        buttonCap2.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeNormal));
        buttonCap3.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeNormal));
        buttonCap4.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeNormal));
        if (answerVariants[0].length() > getResources().getInteger(R.integer.buttonCoreTextChangeToSmallLength)) {
            if (answerVariants[0].length() > getResources().getInteger(R.integer.buttonCoreTextChangeToTinyLength)) {
                buttonCap1.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeTiny));
            } else {
                buttonCap1.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeSmall));
            }
        }
        if (answerVariants[1].length() > getResources().getInteger(R.integer.buttonCoreTextChangeToSmallLength)) {
            if (answerVariants[1].length() > getResources().getInteger(R.integer.buttonCoreTextChangeToTinyLength)) {
                buttonCap2.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeTiny));
            } else {
                buttonCap2.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeSmall));

            }
        }
        if (answerVariants[2].length() > getResources().getInteger(R.integer.buttonCoreTextChangeToSmallLength)) {
            if (answerVariants[2].length() > getResources().getInteger(R.integer.buttonCoreTextChangeToTinyLength)) {
                buttonCap3.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeTiny));
            } else {
                buttonCap3.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeSmall));

            }
        }
        if (answerVariants[3].length() > getResources().getInteger(R.integer.buttonCoreTextChangeToSmallLength)) {
            if (answerVariants[3].length() > getResources().getInteger(R.integer.buttonCoreTextChangeToTinyLength)) {
                buttonCap4.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeTiny));
            } else {
                buttonCap4.setTextSize(getResources().getInteger(R.integer.buttonCoreTextSizeSmall));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.i("alc",  "CoreTest onCreate, countryList  : " + countryList);

        setContentView(R.layout.core_test);

        ivImageCapital = findViewById(R.id.ivImageCapital);


        Resources resources = getResources();
        userName = loadDataUser();
        //lastQuestResult = loadLastResult();
        bestScore = loadBestScore();
        //Log.i("caps",  "lastQuestResult : " + lastQuestResult);
        //Log.i("caps",  "bestScore : " + bestScore);

        buttonCap1 = findViewById(R.id.capital1);
        buttonCap2 = findViewById(R.id.capital2);
        buttonCap3 = findViewById(R.id.capital3);
        buttonCap4 = findViewById(R.id.capital4);
        //Drawable color = buttonCap1.getBackground();



        textViewUsername = findViewById(R.id.username);
        textViewUsername.setText(userName);
        textViewCountryName = findViewById(R.id.country_name);
        testViewQuestionNumber = findViewById(R.id.question_number);
        textViewSec = findViewById(R.id.sec_rest);

        //imageView = findViewById(R.id.image);
        Intent intent = getIntent();
        //pctEasy = intent.getIntExtra(EXTRAS_COUNTRY_CURRENT_PCT_EASY, 0);
        diffLvl = intent.getIntExtra(EXTRAS_DIFFICULT_LVL, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
            countryListCut = intent.getParcelableArrayListExtra(EXTRAS_COUNTRY_CURRENT, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
            countryListCut = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTRY_CURRENT);
        }
        ArrayList<CountryDescribe> cL = new ArrayList<>();
        cL = (ArrayList) countryListCut.stream().filter(e -> e.diffLvl == diffLvl).collect(Collectors.toList());
        countryListExactDiffLevel = (ArrayList) cL.clone();

//        Log.i("caps",  "countryList len start " + countryList.size());
//        Log.i("caps",  "countryListExactDiffLevel len start " + countryListExactDiffLevel.size());
//        Log.i("caps",  "countryListCut len start " + countryListCut.size());



        if (diffLvl == 0) {
            scoreEasy = resources.getInteger(R.integer.easy_points_min);
            scoreMedium = resources.getInteger(R.integer.easy_points_medium);
            scoreHard = resources.getInteger(R.integer.easy_points_max);
        } else if (diffLvl == 1) {
            scoreEasy = resources.getInteger(R.integer.medium_points_min);
            scoreMedium = resources.getInteger(R.integer.medium_points_medium);
            scoreHard = resources.getInteger(R.integer.medium_points_max);
        } else {
            scoreEasy = resources.getInteger(R.integer.hard_points_min);
            scoreMedium = resources.getInteger(R.integer.hard_points_medium);
            scoreHard = resources.getInteger(R.integer.hard_points_max);
        }
        allQuestions = resources.getInteger(R.integer.test_questions);
        timeBestScoreSec = resources.getInteger(R.integer.time_score_best);
        timeMediumScoreSec = resources.getInteger(R.integer.time_score_medium);
        timeMaxSec = resources.getInteger(R.integer.max_time_test_sec);

        View.OnClickListener onClickListener = v -> {
            if (testState.size() < allQuestions) {
                if (v.getId() == R.id.capital1) {
                    if (!timeExpire) {
                        timer.cancel();
                        procAnswer(1);
                    }
                } else if (v.getId() == R.id.capital2) {
                    if (!timeExpire) {
                        timer.cancel();
                        procAnswer(2);
                    }
                } else if (v.getId() == R.id.capital3) {
                    if (!timeExpire) {
                        timer.cancel();
                        procAnswer(3);
                    }
                } else if (v.getId() == R.id.capital4) {
                    if (!timeExpire) {
                        timer.cancel();
                        procAnswer(4);
                    }
                }
            }
        };
        buttonCap1.setOnClickListener(onClickListener);
        buttonCap2.setOnClickListener(onClickListener);
        buttonCap3.setOnClickListener(onClickListener);
        buttonCap4.setOnClickListener(onClickListener);

        runFrameQuest(1);


    }



    private void postUpdateUserScore(String quiz, int score) {
        StringBuilder response = new StringBuilder();
        String jsonInputString = "{\"login\" : \"" + userName + "\"" + ", \"" + BEST_SCORE_FIELD_NAME_DB +  "\" : " + "\"" + score +  "\", \"" +  LASTRES_FIELD_NAME_DB  + "\" : \"" + quiz + "\"}";
        //Log.i("caps",  "postUpdateUserScore jsonInputString : " + jsonInputString);
        URL url;
        try {
            url = new URL(POST_UPDATE_USER);
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
        //return response.toString();
    }

    private String loadDataUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, NOT_LOGGED_USER);
    }

    private String loadLastResult() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getString(LAST_RESULT, "0");
    }

    private int loadBestScore() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(BEST_SCORE, 0);
    }

    private void FillAnswer() {
        boolean found = false;
        int i = 0;
        int randomNum = rand.nextInt(countryListTemp.size());
        while (!found) {
            if (answerVariants[i] == null) {
                answerVariants[i] = countryListTemp.get(randomNum).capitalName;
                found = true;
            } else {
                i++;
            }
        }
        ArrayList<CountryDescribe> cL = new ArrayList<>();
        cL = (ArrayList) countryListTemp.stream().filter(e -> e.id != countryListTemp.get(randomNum).id).collect(Collectors.toList());
        countryListTemp = (ArrayList) cL.clone();
    }

    class TimerTaskSecAnswer extends TimerTask {
        @Override
        public void run() {
            String s = Integer.toString(currTime);
            new Handler(Looper.getMainLooper()).post(() -> setTextViewSecValue(s));
            currTime -= 1;
            if (currTime == -1) {
                new Handler(Looper.getMainLooper()).post(() -> setTextViewSecValue("0"));
                CoreTest.timeExpire = true;
                timer.cancel();
                new Handler(Looper.getMainLooper()).post(() -> procAnswer(0));

            }
        }
    }
    public void updateUserScore(String lastRes, int bestScoreNew) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_RESULT, lastRes);
        if (bestScoreNew > bestScore) {
            editor.putInt(BEST_SCORE, bestScoreNew);
        }
        editor.apply();
        //Toast.makeText(this, "User data updated", Toast.LENGTH_SHORT).show();
    }
}


class UserAnswer  implements Serializable {
    int id, answerNum, score;
    String userAnswer;
    ArrayList<CountryDescribe> countryList;
    String countryName, capitalName;


    @Override
    public String toString() {
        return "UserAnswer{" +
                "id=" + id +
                ", answerNum=" + answerNum +
                ", score=" + score +
                ", countryName='" + countryName + '\'' +
                ", capitalName='" + capitalName + '\'' +
                ", userAnswer='" + userAnswer + '\'' +
                ", countryList=" + countryList +
                '}';
    }

    public String serializeToString() {
        int idUserAnswer = 0;
        if (!this.userAnswer.equals(NO_ANSWER_TIME_EXPIRED)) {
            idUserAnswer = countryList.stream().filter(e -> e.capitalName.equals(this.userAnswer)).findFirst().get().id;
        }
        return "{" + id + "/" + answerNum + "/" + idUserAnswer  + "/" + score + "}";
    }

    public UserAnswer(String s, ArrayList<CountryDescribe> countryList) {
        String temp = s;
        this.id = Integer.parseInt(s.substring(temp.indexOf("{") + 1, temp.indexOf("/")));
        temp = temp.substring(temp.indexOf("/") + 1);
        this.answerNum = Integer.parseInt(temp.substring(0, temp.indexOf("/")));
        temp = temp.substring(temp.indexOf("/") + 1);
        int idAnswer = Integer.parseInt(temp.substring(0, temp.indexOf("/")));

        //Log.i("caps",  "CoreTest, 465, idAnswer : " + idAnswer);
        if (idAnswer == 0) {
            this.userAnswer = NO_ANSWER_TIME_EXPIRED;
        } else {
            this.userAnswer = countryList.stream().filter(e -> e.id == idAnswer).findFirst().get().capitalName;
        }
        temp = temp.substring(temp.indexOf("/") + 1);
        this.score= Integer.parseInt(temp.substring(0, temp.indexOf("}")));
        this.countryName = countryList.stream().filter(e -> e.id == this.id).findFirst().get().countryName;
        this.capitalName = countryList.stream().filter(e -> e.id == this.id).findFirst().get().capitalName;
    }

    public UserAnswer(int id, int answerNum, String userAnswer, int score, ArrayList<CountryDescribe> countryList) {
        this.id = id;
        this.answerNum = answerNum;
        this.userAnswer = userAnswer;
        this.score = score;
        this.countryList = countryList;
        this.capitalName = countryList.stream().filter(e -> e.id == this.id).findFirst().get().capitalName;
    }
}
