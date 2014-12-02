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
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    static public EditText salYear;
    static public EditText salHour;
    static public EditText hrlyWage;
    public static double hoursYear;
    public static double hourlyWage;
    static SharedPreferences mPrefs;
    static String hourlyWageString;
    DataHandler handler;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getActivity().setTitle("Settings");
        final Preference wage = (Preference) findPreference("hourly");
        SwitchPreference salSwitch = (SwitchPreference) findPreference("salBox");
        wage.setEnabled(!salSwitch.isChecked());
        salSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                boolean isEnabled = ((Boolean) o).booleanValue();
                wage.setEnabled(!isEnabled);
                return true;
            }
        });

        //clear
        mPrefs = this.getActivity().getPreferences(Context.MODE_PRIVATE);
        this.getActivity()
                .getSharedPreferences("allTime", Context.MODE_PRIVATE);
        Preference myPref = (Preference) findPreference("clearPref");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Clear All")
                        .setMessage(
                                "Are you sure you want to clear all saved data?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        SharedPreferences.Editor editor = mPrefs.edit();
                                        editor.putString("allTime", "0.00").commit();
                                        editor.putString("hourlyWage", null).commit();
                                        editor.putString("lastTime", "0.00").commit();
                                        openDB();
                                        handler.deleteAll();
                                        //TextView totalNum = (TextView)getActivity(). findViewById(R.id.totalNum);
                                        //totalNum.setText("0.00");

                                    }
                                }
                        )
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).setIcon(R.drawable.ic_action_discard).show();
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

                                String yearly = salYear.getText().toString();
                                String hours = salHour.getText().toString();
                                try {
                                    double hoursDub = Double.parseDouble(hours);
                                    double yearlyDub = Double.parseDouble(yearly);
                                    hoursYear = hoursDub * 52;
                                    hourlyWage = yearlyDub / hoursYear;
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    hourlyWageString = "" + df.format(hourlyWage);
                                    getActivity().getSharedPreferences("hourlyWage", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mPrefs.edit();
                                    editor.putString("hourlyWage", hourlyWageString).commit();
                                    Toast toast = Toast.makeText(getActivity(),
                                            hourlyWageString, Toast.LENGTH_LONG);
                                    //toast.show();
                                } catch (Exception e) {
                                    Toast toast = Toast.makeText(getActivity(),
                                            "Please enter both values!", Toast.LENGTH_LONG);
                                    toast.show();
                                }

                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putString("salary", "0.00").commit();
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
        Preference hrly = (Preference) findPreference("hourly");
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
                                    getActivity().getSharedPreferences("hourlyWage", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mPrefs.edit();
                                    editor.putString("hourlyWage", hourlyWageString).commit();
                                    Toast toast = Toast.makeText(getActivity(),
                                            hourlyWageString, Toast.LENGTH_LONG);
                                    //toast.show();
                                } catch (Exception e) {
                                    Toast toast = Toast.makeText(getActivity(),
                                            "Please enter a value!", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                                SharedPreferences.Editor editor = mPrefs.edit();
                                editor.putString("salary", "0.00").commit();
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
}
