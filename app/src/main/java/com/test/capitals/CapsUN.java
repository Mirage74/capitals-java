package com.test.capitals;

import static com.test.capitals.MainActivity.EXTRAS_COUNTY_LIST;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CapsUN extends AppCompatActivity {
    public static final String CapsListFragmentTag = "CapsListFragmentTag";
    Fragment fragment;
    FrameLayout frameLayout;
    FragmentTransaction ft;
    FragmentManager fm = getSupportFragmentManager();
    ArrayList<CountryDescribe> countryList, countryListFiltered = new ArrayList<>();
    Button modeByCountry, modeByCapital, btBack;
    Button flA, flB, flC, flD, flE, flF, flG, flH, flI, flJ, flK, flL, flM, flN, flO, flP, flQ, flR, flS, flT, flU, flV, flW, flY, flZ;
    int defTextSize, minTextSize, maxTextSize, minFilterChar, rawIdWidth;
    ImageView ivMinus, ivPlus;
    boolean currModeByCountry = true;
    EditText etFilterByCountryName;
    String firstLetterPressed, sFilter;
    Drawable drawable;
    boolean isOrientationLandscape = false;
    Group groupFirstLetter;
    NestedScrollView nestedScrollView;
    ConstraintLayout constraintLayout;
    TextView tvEnterName;


    protected void onCreate(Bundle savedInstanceState) {
        Log.i("alc",  "CapsUN onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caps_un);

//        binding = ResultProfileBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();

        Log.i("alc",  "CapsUN 222 onCreate");
        sFilter = "";
        constraintLayout = findViewById(R.id.constraintlayoutCapsUN);
        etFilterByCountryName = findViewById(R.id.filter);
        frameLayout = findViewById(R.id.fragCapsList);
        modeByCountry = findViewById(R.id.modeByCountry);
        modeByCapital = findViewById(R.id.modeByCapital);
        ivMinus = findViewById(R.id.ivMinus);
        ivPlus = findViewById(R.id.ivPlus);
        btBack = findViewById(R.id.back);
        tvEnterName = findViewById(R.id.info);
        nestedScrollView = findViewById(R.id.nestedScroll);
        groupFirstLetter = findViewById(R.id.groupFirstLetters);
        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            isOrientationLandscape = true;
            tvEnterName.setText(R.string.enter_part_name);
        } else {
            isOrientationLandscape = false;
            findButtonsFirstLetters();
        }

        modeByCountry.setEnabled(false);
        defTextSize = (int) getResources().getDimension(R.dimen.font_size_default_list);
        minTextSize = (int) getResources().getDimension(R.dimen.font_size_min_list);
        maxTextSize = (int) getResources().getDimension(R.dimen.font_size_max_list);
        drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.border, null);
        minFilterChar = getResources().getInteger(R.integer.filterInfoMinLength);
        rawIdWidth = getResources().getInteger(R.integer.listIdWidth);
        Intent intent = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            countryList = intent.getParcelableArrayListExtra(EXTRAS_COUNTY_LIST, CountryDescribe.class);
        } else {
            countryList = (ArrayList<CountryDescribe>) intent.getSerializableExtra(EXTRAS_COUNTY_LIST);
        }

        View.OnClickListener onClickListener = v -> {
            if (v.getId() == R.id.back) {
                Intent intentBack = new Intent(this, MainActivity.class);
                startActivity(intentBack);
            } else if (v.getId() == R.id.modeByCountry) {
                sFilter = "";
                currModeByCountry = true;
                etFilterByCountryName.setText("");
                etFilterByCountryName.setHint(getResources().getString(R.string.enter_country_name));
                modeByCountry.setEnabled(false);
                modeByCapital.setEnabled(true);
                if (!isOrientationLandscape) {
                    refreshFilter(firstLetterPressed);
                    frameLayout.removeAllViews();
                    //newCapsFragment();
                } else {
                    frameLayout.removeAllViews();
                }


            } else if (v.getId() == R.id.modeByCapital) {
                sFilter = "";
                currModeByCountry = false;
                etFilterByCountryName.setText("");
                etFilterByCountryName.setHint(getResources().getString(R.string.enter_capital_name));
                modeByCountry.setEnabled(true);
                modeByCapital.setEnabled(false);
                if (!isOrientationLandscape) {
                    refreshFilter(firstLetterPressed);
                    frameLayout.removeAllViews();
                    newCapsFragment();
                } else {
                    frameLayout.removeAllViews();
                }

            } else if (v.getId() == R.id.ivMinus) {
                if (defTextSize > minTextSize) {
                    defTextSize -= 2;
                    frameLayout.removeAllViews();
                    newCapsFragment();
                }
            } else if (v.getId() == R.id.ivPlus) {
                if (defTextSize < maxTextSize) {
                    defTextSize += 2;
                    frameLayout.removeAllViews();
                    newCapsFragment();
                }
            } else if (!isOrientationLandscape) {
                firstLetterPressed = getFirstLetterPressed(v);
                refreshFilter(firstLetterPressed);
                frameLayout.removeAllViews();
                newCapsFragment();
            }
        };
        btBack.setOnClickListener(onClickListener);
        modeByCountry.setOnClickListener(onClickListener);
        modeByCapital.setOnClickListener(onClickListener);
        ivMinus.setOnClickListener(onClickListener);
        ivPlus.setOnClickListener(onClickListener);

        if (!isOrientationLandscape) {
            setListenerFirstLetters(onClickListener);
            firstLetterPressed = "A";
            refreshFilter(firstLetterPressed);
            newCapsFragment();
        }
        if (!isOrientationLandscape) {


            etFilterByCountryName.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    Log.i("alc", "CapsUN if true hasFocus ");
                    groupFirstLetter.setVisibility(View.INVISIBLE);
                    constraintLayout.removeView(groupFirstLetter);
                    frameLayout.removeAllViews();
                    countryListFiltered.clear();
                    //newCapsFragment();
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    //constraintSet.applyTo(constraintLayout);
                    constraintSet.clear(R.id.back, ConstraintSet.TOP);
                    constraintSet.clear(R.id.back, ConstraintSet.BOTTOM);
                    constraintSet.connect(R.id.back, ConstraintSet.TOP, R.id.nestedScroll, ConstraintSet.BOTTOM, 25);
                    constraintSet.clear(R.id.nestedScroll, ConstraintSet.BOTTOM);
                    constraintSet.connect(R.id.back, ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
                    constraintSet.connect(R.id.nestedScroll, ConstraintSet.BOTTOM, R.id.back, ConstraintSet.TOP);
                    tvEnterName.setText(R.string.enter_part_name);



                    constraintSet.applyTo(constraintLayout);
//                    nestedScrollView.updateViewLayout(this, );

                } else {
                    Log.i("alc", "CapsUN else hasFocus ");
                }
            });
        }

        etFilterByCountryName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                //Log.i("alc",  "CapsUN afterTextChanged " + s);
                sFilter = s.toString();
                frameLayout.removeAllViews();
                if (s.length() >= minFilterChar) {
                    refreshFilter(sFilter);
                    newCapsFragment();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                //Log.i("alc",  "CapsUN beforeTextChanged "+s + " " + start + " " + count + " " + after);
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.i("alc",  "CapsUN onTextChanged "+s + " " + start + " " + before + " " + count);
                sFilter = s.toString();
                if (s.length() >= minFilterChar) {
                    refreshFilter(sFilter);
                    frameLayout.removeAllViews();
                    newCapsFragment();
                }
            }
        });

    }

    private void newCapsFragment() {
        fragment = new com.test.capitals.CapsListFragment();
        ft = fm.beginTransaction().setReorderingAllowed(true);
        ft.replace(R.id.fragCapsList, fragment, CapsListFragmentTag);
        ft.commit();
    }

    private void refreshFilter(String strFilter) {
        if (strFilter.length() == 1) {
            if (currModeByCountry) {
                countryListFiltered = filterListByCountryFirstLetter(strFilter);
            } else {
                countryListFiltered = filterListByCapitalFirstLetter(strFilter);
            }
        } else {
            if (currModeByCountry) {
                countryListFiltered = filterListByCountryUserSearch(strFilter);
            } else {
                countryListFiltered = filterListByCapitalUserSearch(strFilter);
            }
        }

    }

    private ArrayList filterListByCountryFirstLetter(String firstLetter) {
        return (ArrayList) countryList.stream().filter(e -> e.countryName.substring(0, 1).toLowerCase().equals(firstLetter.toLowerCase())).collect(Collectors.toList());
    }

    private ArrayList filterListByCapitalFirstLetter(String firstLetter) {
        return (ArrayList) countryList.stream().filter(e -> e.capitalName.substring(0, 1).toLowerCase().equals(firstLetter.toLowerCase())).collect(Collectors.toList());
    }

    private ArrayList filterListByCountryUserSearch(String searchStr) {
        return (ArrayList) countryList.stream().filter(e -> e.countryName.toLowerCase().indexOf(searchStr.toLowerCase()) >= 0).collect(Collectors.toList());
    }

    private ArrayList filterListByCapitalUserSearch(String searchStr) {
        return (ArrayList) countryList.stream().filter(e -> e.capitalName.toLowerCase().indexOf(searchStr.toLowerCase()) >= 0).collect(Collectors.toList());
    }

    private void findButtonsFirstLetters() {
        flA = findViewById(R.id.firstLetterA);
        flB = findViewById(R.id.firstLetterB);
        flC = findViewById(R.id.firstLetterC);
        flD = findViewById(R.id.firstLetterD);
        flE = findViewById(R.id.firstLetterE);
        flF = findViewById(R.id.firstLetterF);
        flG = findViewById(R.id.firstLetterG);
        flH = findViewById(R.id.firstLetterH);
        flI = findViewById(R.id.firstLetterI);
        flJ = findViewById(R.id.firstLetterJ);
        flK = findViewById(R.id.firstLetterK);
        flL = findViewById(R.id.firstLetterL);
        flM = findViewById(R.id.firstLetterM);
        flN = findViewById(R.id.firstLetterN);
        flO = findViewById(R.id.firstLetterO);
        flP = findViewById(R.id.firstLetterP);
        flQ = findViewById(R.id.firstLetterQ);
        flR = findViewById(R.id.firstLetterR);
        flS = findViewById(R.id.firstLetterS);
        flT = findViewById(R.id.firstLetterT);
        flU = findViewById(R.id.firstLetterU);
        flV = findViewById(R.id.firstLetterV);
        flW = findViewById(R.id.firstLetterW);
        flY = findViewById(R.id.firstLetterY);
        flZ = findViewById(R.id.firstLetterZ);
    }

    private void setListenerFirstLetters(View.OnClickListener onClickListener) {
        flA.setOnClickListener(onClickListener);
        flB.setOnClickListener(onClickListener);
        flC.setOnClickListener(onClickListener);
        flD.setOnClickListener(onClickListener);
        flE.setOnClickListener(onClickListener);
        flF.setOnClickListener(onClickListener);
        flG.setOnClickListener(onClickListener);
        flH.setOnClickListener(onClickListener);
        flI.setOnClickListener(onClickListener);
        flJ.setOnClickListener(onClickListener);
        flK.setOnClickListener(onClickListener);
        flL.setOnClickListener(onClickListener);
        flM.setOnClickListener(onClickListener);
        flN.setOnClickListener(onClickListener);
        flO.setOnClickListener(onClickListener);
        flP.setOnClickListener(onClickListener);
        flQ.setOnClickListener(onClickListener);
        flR.setOnClickListener(onClickListener);
        flS.setOnClickListener(onClickListener);
        flT.setOnClickListener(onClickListener);
        flU.setOnClickListener(onClickListener);
        flV.setOnClickListener(onClickListener);
        flW.setOnClickListener(onClickListener);
        flY.setOnClickListener(onClickListener);
        flZ.setOnClickListener(onClickListener);
    }

    private String getFirstLetterPressed(View v) {
        String letterPressed = "";
        if (v.getId() == R.id.firstLetterA) {
            letterPressed = "A";
        } else if (v.getId() == R.id.firstLetterB) {
            letterPressed = "B";
        } else if (v.getId() == R.id.firstLetterC) {
            letterPressed = "C";
        } else if (v.getId() == R.id.firstLetterD) {
            letterPressed = "D";
        } else if (v.getId() == R.id.firstLetterE) {
            letterPressed = "E";
        } else if (v.getId() == R.id.firstLetterF) {
            letterPressed = "F";
        } else if (v.getId() == R.id.firstLetterG) {
            letterPressed = "G";
        } else if (v.getId() == R.id.firstLetterH) {
            letterPressed = "H";
        } else if (v.getId() == R.id.firstLetterI) {
            letterPressed = "I";
        } else if (v.getId() == R.id.firstLetterJ) {
            letterPressed = "J";
        } else if (v.getId() == R.id.firstLetterK) {
            letterPressed = "K";
        } else if (v.getId() == R.id.firstLetterL) {
            letterPressed = "L";
        } else if (v.getId() == R.id.firstLetterM) {
            letterPressed = "M";
        } else if (v.getId() == R.id.firstLetterN) {
            letterPressed = "N";
        } else if (v.getId() == R.id.firstLetterO) {
            letterPressed = "O";
        } else if (v.getId() == R.id.firstLetterP) {
            letterPressed = "P";
        } else if (v.getId() == R.id.firstLetterQ) {
            letterPressed = "Q";
        } else if (v.getId() == R.id.firstLetterR) {
            letterPressed = "R";
        } else if (v.getId() == R.id.firstLetterS) {
            letterPressed = "S";
        } else if (v.getId() == R.id.firstLetterT) {
            letterPressed = "T";
        } else if (v.getId() == R.id.firstLetterU) {
            letterPressed = "U";
        } else if (v.getId() == R.id.firstLetterV) {
            letterPressed = "V";
        } else if (v.getId() == R.id.firstLetterW) {
            letterPressed = "W";
        } else if (v.getId() == R.id.firstLetterY) {
            letterPressed = "Y";
        } else if (v.getId() == R.id.firstLetterZ) {
            letterPressed = "Z";
        }
        return letterPressed;
    }
}




