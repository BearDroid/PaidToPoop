package com.beardroid.paid2poop;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Max on 11/13/2014.
 */
public class HomeFragment extends Fragment {
    public SharedPreferences mPrefs;
    public ArrayList<PoopCard> list = new ArrayList<PoopCard>();
    DataHandler handler;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        final ListView myList = (ListView) view.findViewById(R.id.pooplist);
        openDB();
        populateListViewFromDB(myList);

        FloatingActionButton fab = new FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.ic_action_money))
                .withButtonColor(Color.parseColor("#0e8f4f"))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        //fab.attachToListView(listView);
        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        //FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final View alertView = inflater.inflate(R.layout.poop_dialog, (ViewGroup) getActivity().findViewById(R.id.poopdialog));
                builder.setView(alertView);
                NumberPicker np = (NumberPicker) alertView.findViewById(R.id.numberpicker);
                np.setMaxValue(120);
                np.setMinValue(1);
                np.setValue(1);
                builder.setPositiveButton("Add!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //rating button
                        RadioGroup ratingGroup = (RadioGroup) alertView.findViewById(R.id.ratingGroup);
                        RadioButton ratingButton;
                        int selectedRating = ratingGroup.getCheckedRadioButtonId();
                        ratingButton = (RadioButton) alertView.findViewById(selectedRating);

                        String ratingString = (String) ratingButton.getText();

                        //number spinner
                        NumberPicker np = (NumberPicker) alertView.findViewById(R.id.numberpicker);
                        String num = "" + np.getValue();
                        Double minDbl = Double.parseDouble(num);

                        //get sharedpref of hourly wage and do the magic math here

                        String wageStr = mPrefs.getString("hourlyWage", null); //get saved wage
                        Double wageDbl = Double.parseDouble(wageStr); //saved wage to double
                        minDbl = minDbl / 60;
                        double moneyMade = minDbl * wageDbl;
                        DecimalFormat moneyFormat = new DecimalFormat("0.00");
                        String madeStr = "" + moneyFormat.format(moneyMade);
                        moneyMade = Double.parseDouble(madeStr);


                        //this does time
                        Calendar cal = Calendar.getInstance();
                        int day = cal.get(Calendar.DATE);
                        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
                        int year = cal.get(Calendar.YEAR);
                        String date = month + " " + day + ", " + year;
                        int hour = cal.get(Calendar.HOUR);
                        Format timeFormat = new DecimalFormat("00");
                        int min = cal.get(Calendar.MINUTE);
                        String minute = timeFormat.format(min);
                        int ampm = cal.get(Calendar.AM_PM);
                        String ampmString;
                        if (ampm == 0) { //translates ampm to a readable string because 0 or 1 doesn't fucking help
                            ampmString = "AM";
                        } else {
                            ampmString = "PM";
                        }
                        String time = hour + ":" + minute + " " + ampmString;
                        //Toast.makeText(getActivity(), madeStr, Toast.LENGTH_LONG).show();
                        handler.insertData(moneyMade, date, time, ratingString);
                        Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), jsonPoop, Toast.LENGTH_LONG).show();
                        populateListViewFromDB(myList);
                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //do nothing
                            }
                        })
                        .show();
            }
        });
        return view;
    }

    //here lie database stuff
    private void openDB() {
        handler = new DataHandler(getActivity());
        handler.open();
    }

    private void closeDB() {
        handler.close();
    }
    private void makeHeaderNumber(TextView header){
        Cursor cursor = handler.getAllRows();

        String[] amountsStored = new String[]{
                DataHandler.KEY_AMOUNT
        };
        double totalAmount = 0;
        for(int i=0; i<amountsStored.length; i++){
            double add = Double.parseDouble(amountsStored[i]);
            totalAmount = totalAmount + add;
        }


    }
    private void populateListViewFromDB(ListView myList) {
        Cursor cursor = handler.getAllRows();
        //gets data from db
        String[] fromFieldNames = new String[]{
                DataHandler.KEY_AMOUNT, DataHandler.KEY_DATE, DataHandler.KEY_TIME, DataHandler.KEY_RATING
        };
        //gets ids of textviews
        int[] toViewIDs = new int[]{
                R.id.money, R.id.date, R.id.time, R.id.smiley
        };
        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        getActivity(),        // Context
                        R.layout.poopcard,    // Row layout template
                        cursor,                    // cursor (set of DB records to map)
                        fromFieldNames,            // DB Column names
                        toViewIDs                // View IDs to put information in
                );

        myList.setAdapter(myCursorAdapter);
    }
}
