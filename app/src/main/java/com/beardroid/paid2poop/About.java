package com.beardroid.paid2poop;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Max on 11/13/2014.
 */
public class About extends Fragment {
    private Toolbar mToolbar;

    public About() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        getActivity().setTitle("About");
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        TextView play = (TextView) view.findViewById(R.id.play);
        play.setClickable(true);
        play.setMovementMethod(LinkMovementMethod.getInstance());
        String playtext = "<a href='https://play.google.com/store/apps/details?id=com.beardroid.paid2poop'>Play</a>";
        play.setText(Html.fromHtml(playtext));
        //link to twitter
        TextView twitter = (TextView) view.findViewById(R.id.twitter);
        twitter.setClickable(true);
        twitter.setMovementMethod(LinkMovementMethod.getInstance());
        String twittertext = "<a href='https://twitter.com/BearDroid77'>Twitter</a>";
        twitter.setText(Html.fromHtml(twittertext));
        //link to google+
        TextView plus = (TextView) view.findViewById(R.id.gplus);
        plus.setClickable(true);
        plus.setMovementMethod(LinkMovementMethod.getInstance());
        String plustext = "<a href='https://plus.google.com/+MaxMoss/posts'>Google+</a>";
        plus.setText(Html.fromHtml(plustext));

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                View alertView = inflater.inflate(R.layout.whatsnew, (ViewGroup) getActivity().findViewById(R.id.newstuff));
                alert.setView(alertView);
                alert.setNeutralButton("Cool!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).show();
            }
        });
        return view;
    }


}

