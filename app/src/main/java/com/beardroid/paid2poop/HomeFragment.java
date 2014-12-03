package com.beardroid.paid2poop;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Max on 11/13/2014.
 */
public class HomeFragment extends Fragment{
    public SharedPreferences mPrefs;
    public ArrayList<PoopCard> list = new ArrayList<PoopCard>();
    DataHandler handler;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Toolbar mToolbar;
    public Context context = getActivity();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        openDB();

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pooplist);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        populateListViewFromDB(recyclerView);
        makeHeaderNumber(view);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        final FloatingActionsMenu mainFab = (FloatingActionsMenu) view.findViewById(R.id.mainFab);
        FloatingActionButton editFab = (FloatingActionButton) view.findViewById(R.id.editFab);
        FloatingActionButton timerFab = (FloatingActionButton) view.findViewById(R.id.timerFab);
        editFab.setSize(1);
        timerFab.setSize(1);
        mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        editFab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public void onClick(final View view) {
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
                        String madeStr = moneyFormat.format(moneyMade);
                        moneyMade = Double.parseDouble(madeStr);
                        madeStr = "$" + madeStr;

                        String date = dateGetter();
                        String time = timeGetter();
                        handler.insertData(moneyMade, date, time, ratingString, madeStr);
                        populateListViewFromDB(recyclerView);
                        makeHeaderNumber(getView());

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

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.pooplist);
        populateListViewFromDB(recyclerView);
        makeHeaderNumber(getView());
        super.onResume();
    }

    public String timeGetter() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        if (hour == 0) {
            hour = 12;
        }
        Format timeFormat = new DecimalFormat("00");
        int min = cal.get(Calendar.MINUTE);
        String minute = timeFormat.format(min);
        int ampm = cal.get(Calendar.AM_PM);
        String ampmString;
        if (ampm == 0) { //translates ampm to a readable string because 0 or 1 doesn't help
            ampmString = "AM";
        } else {
            ampmString = "PM";
        }
        String time = hour + ":" + minute + " " + ampmString;
        return time;
    }

    public String dateGetter() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        String month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
        int year = cal.get(Calendar.YEAR);
        String date = month + " " + day + ", " + year;
        return date;
    }

    //here lie database stuff
    private void openDB() {
        handler = new DataHandler(getActivity());
        handler.open();
    }

    private void closeDB() {
        handler.close();
    }

    private void makeHeaderNumber(View view) {
        Cursor c = handler.totalAmount();
        Double totalAmount = c.getDouble(c.getColumnIndex("myTotal"));
        DecimalFormat moneyFormat = new DecimalFormat("0.00");
        String totalAmountStr = moneyFormat.format(totalAmount);
        totalAmountStr = "Total: $" + totalAmountStr;
        getActivity().setTitle(totalAmountStr);
    }

    private void populateListViewFromDB(RecyclerView myList) {
        List<PoopCard> poopCardList = handler.getPoopCardInfo();
        mAdapter = new PoopCardAdapter(poopCardList, getActivity());
        myList.setAdapter(mAdapter);
    }
}
