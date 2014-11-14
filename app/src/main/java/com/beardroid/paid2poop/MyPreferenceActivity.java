package com.beardroid.paid2poop;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Max on 11/14/2014.
 */
public class MyPreferenceActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_with_actionbar);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(com.beardroid.paid2poop.R.id.toolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();
    }
}
