package com.beardroid.paid2poop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by Max on 11/14/2014.
 */
public class MyPreferenceFragment extends PreferenceFragment {
    public static final String MY_PREFS = "myPrefs";
    public static final String OVERTIME_PREF = "overtime";
    public static final String ARE_YOU_SURE = "Are you sure you want to flush all of your poops and wage info?";
    public static final String ALL_TIME_PREF = "allTime";
    public static final String HOURLY_WAGE_PREF = "hourlyWage";
    public static final String SALARY_HOURS_PREF = "salhours";
    public static final String SALARY_PREF = "salary";
    public static final String SETTINGS_TITLE = "Settings";
    public static final String HOURLY = "hourly";

    static public EditText salYear;
    static public EditText salHour;
    static public EditText hrlyWage;
    public static double hoursYear;
    public static double hourlyWage;
    public static SharedPreferences pref;
    static String hourlyWageString;
    DataHandler handler;
    boolean isEnabled;
    boolean isOvertime;
    Preference current;
    SwitchPreference salSwitch;
    SwitchPreference overtime;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        pref = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        getActivity().setTitle(SETTINGS_TITLE);

        final SharedPreferences.Editor edit = pref.edit();

        final Preference wage = (Preference) findPreference(HOURLY);
        final Preference current = findPreference("current");
        salSwitch = (SwitchPreference) findPreference("salBox");
        overtime = (SwitchPreference) findPreference("overtime");

        wage.setEnabled(!salSwitch.isChecked());
        isSalary();
        setCurrentHourly();
        setCurrentSalary();

        salSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                isEnabled = (Boolean) o; //true means salary mode is on
                wage.setEnabled(!isEnabled); //if it's not true, then wage mode is on
                if (isEnabled) {
                    setCurrentSalary();

                }
                if (!isEnabled) {
                    setCurrentHourly();
                }
                return true;
            }
        });
        overtime.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                isOvertime = (Boolean) o; //true means overtime mode is on
                if (isOvertime) {
                    edit.putBoolean(OVERTIME_PREF, true);

                }
                if (!isOvertime) {
                    edit.putBoolean(OVERTIME_PREF, false);
                }
                edit.apply();
                return true;
            }
        });

        //clear
        Preference myPref = (Preference) findPreference("clearPref");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Clear All")
                        .setMessage(
                                ARE_YOU_SURE)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        openDB();
                                        handler.deleteAll();
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.remove(ALL_TIME_PREF);
                                        editor.remove(HOURLY_WAGE_PREF);
                                        editor.remove(SALARY_HOURS_PREF);
                                        editor.remove(SALARY_PREF);
                                        editor.apply();
                                        current.setTitle(R.string.currentTitle);
                                        current.setSummary(R.string.currentSum);
                                    }
                                }
                        )
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).setIcon(R.drawable.ic_delete_grey).show();
                return true;
            }
        });
        //salary setting
        Preference perYear = (Preference) findPreference("salaryMoney");
        perYear.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                View alertView = inflater.inflate(R.layout.salaryview, (ViewGroup) getActivity().findViewById(R.id.salLayout));
                salYear = (EditText) alertView.findViewById(R.id.salaryEdit);
                salHour = (EditText) alertView.findViewById(R.id.hourEdit);
                alert.setView(alertView);
                alert.setTitle("Enter salary info");
                alert.setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                String salary = salYear.getText().toString();
                                String hours = salHour.getText().toString();
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(SALARY_PREF, salary);
                                editor.putString(SALARY_HOURS_PREF, hours);
                                editor.apply();
                                int hourInt = Integer.parseInt(hours);
                                if (hourInt == 0) {
                                    Toast.makeText(getActivity(), "You work 0 hours per week? Must be nice.", Toast.LENGTH_SHORT).show();
                                } else if (salary.length() > 0 && hours.length() > 0) {
                                    double hoursDub = Double.parseDouble(hours);
                                    double yearlyDub = Double.parseDouble(salary);
                                    hoursYear = hoursDub * 52;
                                    hourlyWage = yearlyDub / hoursYear;
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    hourlyWageString = "" + df.format(hourlyWage);
                                    getActivity().getSharedPreferences(HOURLY_WAGE_PREF, Context.MODE_PRIVATE);
                                    editor.putString(HOURLY_WAGE_PREF, hourlyWageString).apply();
                                    setCurrentSalary();
                                    editor.putString(SALARY_PREF, salary);
                                    editor.putString(SALARY_HOURS_PREF, hours);
                                    editor.apply();
                                } else {
                                    Toast toast = Toast.makeText(getActivity(),
                                            "Please enter both values!", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                                //TextView totalNum = (TextView)getActivity(). findViewById(R.id.totalNum);
                                //totalNum.setText("0.00");

                            }
                        }
                )
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
                return false;
            }

        });
        //hourly wage settings
        Preference hrly = (Preference) findPreference(HOURLY);
        hrly.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                View alertView = inflater.inflate(R.layout.hrlyview, (ViewGroup) getActivity().findViewById(R.id.hrlyLayout));
                hrlyWage = (EditText) alertView.findViewById(R.id.hrlyEdit);
                alert.setView(alertView);
                alert.setTitle("Enter hourly wage info");
                alert.setPositiveButton("Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String wage = hrlyWage.getText().toString();
                                try {
                                    double wageDub = Double.parseDouble(wage);
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    hourlyWageString = "" + df.format(wageDub);
                                    getActivity().getSharedPreferences(HOURLY_WAGE_PREF, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(HOURLY_WAGE_PREF, hourlyWageString).apply();

                                } catch (Exception e) {
                                    Toast toast = Toast.makeText(getActivity(),
                                            "Please enter a value!", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString(SALARY_HOURS_PREF, null).apply();
                                editor.putString(SALARY_PREF, null).apply();
                                setCurrentHourly();
                            }
                        }
                )
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
                return false;
            }
        });
    }

    private void openDB() {
        handler = new DataHandler(getActivity());
        handler.open();
    }

    public void isSalary() {
        String check = pref.getString(SALARY_PREF, "nada");
        boolean checker = true;
        if (check.equals("nada"))
            checker = false;
        salSwitch.setChecked(checker);
    }

    public void setUp() {
        String wage = pref.getString(HOURLY_WAGE_PREF, "nada");
        String salary = pref.getString(SALARY_PREF, "nada");
        if (!wage.equals("nada") && salary.equals("nada")) {
            setCurrentHourly();
        }
        if (wage.equals("nada") && !salary.equals("nada")) {
            setCurrentSalary();
        }
    }

    public void setCurrentSalary() {
        String hrlyWage = pref.getString(HOURLY_WAGE_PREF, "error");

        boolean hasWage = (hrlyWage != null);
        if (hasWage) {
            current = findPreference("current");
            String salary = pref.getString(SALARY_PREF, "error");
            String hours = pref.getString(SALARY_HOURS_PREF, "error");
            if (!salary.equals("0.00") && !hours.equals("error")) {
                current.setTitle("Salary Mode");
                current.setSummary("Your salary is set at $" + salary + " per year at " + hours + " hours per week.");
            }
        }
    }

    public void setCurrentHourly() {
        current = findPreference("current");
        String hrlyWage = pref.getString(HOURLY_WAGE_PREF, "0.00");
        Double hrlyDbl = Double.parseDouble(hrlyWage);
        boolean hasWage = (hrlyWage != null);
        if (hasWage) {
            if (hrlyDbl > 0) {
                current.setTitle("Hourly Wage Mode");
                current.setSummary("Your hourly wage is set at $" + hrlyWage + " per hour.");
            }
        }
    }
}
