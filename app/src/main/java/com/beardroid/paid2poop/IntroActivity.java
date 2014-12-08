package com.beardroid.paid2poop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by Max on 12/6/2014.
 */
public class IntroActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String MY_PREFS = "myPrefs";
    public SharedPreferences pref;
    Button salaryBtn;
    Button hourlyBtn;
    Button continueBtn;
    EditText salaryEditText;
    EditText hourlyEditText; //used for hourly wage
    EditText hoursEditText; //used for salary hours (yeah I couldn't think of a better name)
    LinearLayout hourlyLayout;
    LinearLayout salaryLayout;
    boolean isHourly;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_view);
        salaryBtn = (Button) findViewById(R.id.salaryBtn);
        hourlyBtn = (Button) findViewById(R.id.hourlyBtn);
        continueBtn = (Button) findViewById(R.id.continueBtn);
        salaryEditText = (EditText) findViewById(R.id.salaryEditText);
        hourlyEditText = (EditText) findViewById(R.id.hourlyEditText); //hourly wage
        hoursEditText = (EditText) findViewById(R.id.hoursEditText); //salary hours
        hourlyLayout = (LinearLayout) findViewById(R.id.hourlyLayout);
        salaryLayout = (LinearLayout) findViewById(R.id.salary);
        isHourly = true;
        pref = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = pref.edit();


        salaryBtn.setOnClickListener(this);
        hourlyBtn.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.salaryBtn:
                clickSalary();
                break;
            case R.id.hourlyBtn:
                clickHourly();
                break;
            case R.id.continueBtn:
                clickContinue();
                break;
        }
    }

    public void clickSalary() {
        isHourly = false;
        salaryLayout.setVisibility(View.VISIBLE);
        hourlyLayout.setVisibility(View.GONE);
    }

    public void clickHourly() {
        isHourly = true;
        salaryLayout.setVisibility(View.GONE);
        hourlyLayout.setVisibility(View.VISIBLE);

    }

    public void clickContinue() {
        if (isHourly) {
            String hourlyWage = hourlyEditText.getText().toString();
            if(hourlyWage.length()> 0) {
                editor.putString("hourlyWage", hourlyWage);
                editor.apply();
                finish();
            } else{
                Toast.makeText(this, "Please enter your information!", Toast.LENGTH_LONG).show();
            }
        }
        if (!isHourly) {
            String salary = salaryEditText.getText().toString();
            String hours = hoursEditText.getText().toString();
            if(salary.length() > 0 && hours.length() > 0){
                Double salaryDbl = Double.parseDouble(salary);
                Double hoursDbl = Double.parseDouble(hours);
                Double hoursYearly = hoursDbl *52;
                Double hourlyWage = salaryDbl/hoursYearly;
                DecimalFormat df = new DecimalFormat("0.00");
                String hourlyWageStr = "" + df.format(hourlyWage);
                editor.putString("hourlyWage", hourlyWageStr);
                editor.putString("salary", salary);
                editor.putString("salHours", hours);
                editor.apply();
                finish();
            } else {
                Toast.makeText(this, "Please enter both numbers!", Toast.LENGTH_LONG).show();
            }
        }

    }
}
