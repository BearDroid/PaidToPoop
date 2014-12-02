package com.beardroid.paid2poop;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by Max on 12/1/2014.
 */
public class DetailActivity extends ActionBarActivity {
    Toolbar mToolbar;
    RelativeLayout topDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        topDetail = (RelativeLayout) findViewById(R.id.topDetail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setBackgroundColor(Color.DKGRAY);
        setBackgroundColor();
        this.setTitle(null);
        populateData();

    }
    public void setBackgroundColor(){
        String rating = getIntent().getExtras().getString("Rating");
        if(rating.equals("Good")){
            mToolbar.setBackgroundColor(Color.parseColor("#2196F3"));
            topDetail.setBackgroundColor(Color.parseColor("#2196F3"));
            setStatusGood();
        } else if(rating.equals("Okay")){
            mToolbar.setBackgroundColor(Color.parseColor("#9C27B0"));
            topDetail.setBackgroundColor(Color.parseColor("#9C27B0"));
            setStatusOkay();
        } else if(rating.equals("Bad")){
            mToolbar.setBackgroundColor(Color.parseColor("#F44336"));
            topDetail.setBackgroundColor(Color.parseColor("#F44336"));
            setStatusBad();
        }
    }
    public void populateData(){
        TextView amountView = (TextView) findViewById(R.id.amountDetail);
        String amt = getIntent().getExtras().getString("Amount");
        Double amtDbl = Double.parseDouble(amt);
        DecimalFormat moneyFormat = new DecimalFormat("0.00");
        amt = moneyFormat.format(amtDbl);
        amt = "$" + amt;
        amountView.setText(amt);

        TextView dateView = (TextView) findViewById(R.id.dateText);
        dateView.setText(getIntent().getExtras().getString("Date"));

        TextView timeView = (TextView) findViewById(R.id.timeText);
        timeView.setText(getIntent().getExtras().getString("Time"));
    }
    //below sets statusbar if on Lollipop
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusGood(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#1976D2"));
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusOkay(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#7B1FA2"));
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBad(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#D32F2F"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.action_delete:
                delete();
                return true;
            case R.id.action_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
    public void delete(){

    }
    public void share(){

    }
}
