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
import static com.test.capitals.MainActivity.BEST_RESULT;
import static com.test.capitals.MainActivity.BEST_SCORE;
import static com.test.capitals.MainActivity.CORE_STATE;
import static com.test.capitals.MainActivity.CORE_NEW;
import static com.test.capitals.MainActivity.CORE_LAST_QUESTION_ID;
import static com.test.capitals.MainActivity.CORE_SEC_REST;
import static com.test.capitals.MainActivity.CORE_AV1;
import static com.test.capitals.MainActivity.CORE_AV2;
import static com.test.capitals.MainActivity.CORE_AV3;
import static com.test.capitals.MainActivity.CORE_AV4;
import static com.test.capitals.MainActivity.CORE_RA;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

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
    Boolean isNewTest = true;

    ArrayList<CountryDescribe> countryList, countryListCut, countryListExactDiffLevel, countryListTemp;
    ArrayList<UserAnswer> testState = new ArrayList<>();;
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
    int allQuestions, scoreEasy, scoreMedium, scoreHard, diffLvl, sumScore = 0, lastQuestionId, lastSecRest;
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
            runFrameQuest(testState.size() + 1, 0);
        } else {
            String arrStr = "";
            for (int i = 0; i < testState.size(); i++) {
                arrStr = arrStr + testState.get(i).serializeToString(countryList);
            }

            postUpdateUserScore(arrStr, sumScore);
            updateUserScore(arrStr, sumScore);
            Intent intent = new Intent(this, ScoreMain.class);
            intent.putExtra(EXTRAS_COUNTY_LIST, countryList);
            startActivity(intent);
            CoreTest.this.finish();
        }
    }



    @Override
    public void onDestroy(){
        if ( testState != null && testState.size() > 0 && testState.size() < allQuestions)  {
            saveTestState(testState);
        }
        timer.cancel();
        super.onDestroy();
    }

    private void runFrameQuest(int questNum, int lastQuestionId) {
        int int_random = 0;
        timeExpire = false;
//        Log.i("alc",  "CoreTest lastQuestionId  : " + lastQuestionId);
//        Log.i("alc",  "CoreTest lastSecRest  : " + lastSecRest);
//        Log.i("alc",  "CoreTest countryListExactDiffLevel.size  : " + countryListExactDiffLevel.size());
//        Log.i("alc",  "CoreTest countryListCut.size  : " + countryListCut.size());
        if (questNum % 2 == 1) {

            if (lastQuestionId > 0) {
                testCountry = countryList.stream().filter(e -> e.id == lastQuestionId).findFirst().get();
                //int_random = countryListExactDiffLevel.indexOf(testCountry);
            } else {
                int_random = rand.nextInt(countryListExactDiffLevel.size());
                testCountry = countryListExactDiffLevel.get(int_random);
            }


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
            if (lastQuestionId == 0) {
                countryListExactDiffLevel.remove(int_random);
            }
            ArrayList<CountryDescribe> cL = new ArrayList<>();
            cL = (ArrayList) countryListCut.stream().filter(e -> e.id != testCountry.id).collect(Collectors.toList());
            countryListCut = (ArrayList) cL.clone();
            countryListTemp = new ArrayList<>();
            countryListTemp = (ArrayList) countryList.clone();
        } else {
            if (lastQuestionId > 0) {
                testCountry = countryListCut.stream().filter(e -> e.id == lastQuestionId).findFirst().get();

                int_random = countryListCut.indexOf(testCountry);
            } else {
                int_random = rand.nextInt(countryListCut.size());
                testCountry = countryListCut.get(int_random);
            }

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
            if (lastQuestionId == 0) {
                countryListCut.remove(int_random);
            }
            ArrayList<CountryDescribe> cL = new ArrayList<>();
            cL = (ArrayList) countryListExactDiffLevel.stream().filter(e -> e.id != testCountry.id).collect(Collectors.toList());
            countryListExactDiffLevel = (ArrayList) cL.clone();
            countryListTemp = new ArrayList<>();
            countryListTemp = (ArrayList) countryList.clone();
        }
        if (lastQuestionId > 0) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            answerVariants[0] = sharedPreferences.getString(CORE_AV1, "1");
            answerVariants[1] = sharedPreferences.getString(CORE_AV2, "2");
            answerVariants[2] = sharedPreferences.getString(CORE_AV3, "3");
            answerVariants[3] = sharedPreferences.getString(CORE_AV4, "4");
            rightAnswerNum = sharedPreferences.getInt(CORE_RA, 0);
        } else {
            rightAnswerNum = rand.nextInt(3);
            for (int i = 0; i < 4; i++) {
                answerVariants[i] = null;
            }
            answerVariants[rightAnswerNum] = testCountry.capitalName;

            countryListTemp = (ArrayList) countryList.stream().filter(e -> e.id != testCountry.id).collect(Collectors.toList());
            FillAnswer();
            FillAnswer();
            FillAnswer();
        }

        fitTextButton();
        buttonCap1.setText(answerVariants[0]);
        buttonCap2.setText(answerVariants[1]);
        buttonCap3.setText(answerVariants[2]);
        buttonCap4.setText(answerVariants[3]);
        Picasso.get().load(BACKEND_URL + "/" + testCountry.imageName).into(ivImageCapital);
        if (lastQuestionId > 0) {
            if (lastSecRest > 1) {
                currTime = lastSecRest;
            } else {
                currTime = 1;
            }
        } else {
            currTime = timeMaxSec;
        }

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

    //    Log.i("alc",  "CoreTest onCreate, countryList  : " + countryList);

        setContentView(R.layout.core_test);
        lastQuestionId = 0;

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

        int i = loadIsNewTest();
        if (i == 0) {
            isNewTest = false;
            lastQuestionId = loadLastQuestionId();
            lastSecRest = loadSecRest();

        } else {
            isNewTest = true;

        }
        //Log.i("alc",  "core isNewTest " + isNewTest);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
            countryListCut = intent.getParcelableArrayListExtra(EXTRAS_COUNTRY_CURRENT, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
            countryListCut = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTRY_CURRENT);
        }
        //Log.i("alc",  " core teststate " + loadTestState(countryList));
        if (!isNewTest) {
            testState = loadTestState(countryList);
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


        runFrameQuest(testState.size() + 1, lastQuestionId);


    }

    public ArrayList<UserAnswer> loadTestState(ArrayList<CountryDescribe> countryList) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String cS = sharedPreferences.getString(CORE_STATE, "");
        return ScoreMain.parseStringToUserAnswerList(cS, countryList);
    }

    public int loadIsNewTest() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int i = sharedPreferences.getInt(CORE_NEW, 1);
        return i;
    }

    public int loadLastQuestionId() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int i = sharedPreferences.getInt(CORE_LAST_QUESTION_ID, 0);
        return i;
    }

    public int loadSecRest() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        int i = sharedPreferences.getInt(CORE_SEC_REST, 0);
        return i;
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

//    private String loadLastResult() {
//        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        return sharedPreferences.getString(LAST_RESULT, "0");
//    }

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
            editor.putString(BEST_RESULT, lastRes);
        }
        editor.apply();
        //Toast.makeText(this, "User data updated", Toast.LENGTH_SHORT).show();
    }

    public void saveTestState(ArrayList<UserAnswer> testState) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String arrStr = "";
        for (int i = 0; i < testState.size(); i++) {
            arrStr = arrStr + testState.get(i).serializeToString(countryList);
        }
        editor.putString(CORE_STATE, arrStr);
        editor.putInt(CORE_LAST_QUESTION_ID, testCountry.id);
        editor.putInt(CORE_NEW, 0);
        editor.putInt(CORE_SEC_REST, Integer.parseInt((String) textViewSec.getText()));
        editor.putString(CORE_AV1, answerVariants[0]);
        editor.putString(CORE_AV2, answerVariants[1]);
        editor.putString(CORE_AV3, answerVariants[2]);
        editor.putString(CORE_AV4, answerVariants[3]);
        editor.putInt(CORE_RA, rightAnswerNum);
        editor.apply();
    }



    public void onResume(Bundle savedInstanceState) {
        super.onResume();
        Log.i("alc",  "CoreTest onResume");
    }

    public void onStart(Bundle savedInstanceState) {
        super.onStart();
        Log.i("alc",  "CoreTest onStart");
    }

    public void onPause(Bundle savedInstanceState) {
        super.onPause();
        Log.i("alc",  "CoreTest onPause");
    }

    public void onStop(Bundle savedInstanceState) {
        super.onStop();
        Log.i("alc",  "CoreTest onStop");
    }

    public void onDestroy(Bundle savedInstanceState) {
        super.onDestroy();
        Log.i("alc",  "CoreTest onDestroy");
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




    public String serializeToString(ArrayList<CountryDescribe> countryList) {
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
