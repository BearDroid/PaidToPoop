package com.beardroid.paid2poop;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
        Button play = (Button) view.findViewById(R.id.play);
        Button twitter = (Button) view.findViewById(R.id.twitter);
        Button plus = (Button) view.findViewById(R.id.gplus);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://play.google.com/store/apps/details?id=com.beardroid.paid2poop";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/BearDroid77";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://plus.google.com/+MaxMoss/posts";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                View alertView = inflater.inflate(R.layout.whatsnew, (ViewGroup) getActivity().findViewById(R.id.newstuff));
                alert.setView(alertView);
                alert.setPositiveButton("Cool!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).show();
            }
        });
        return view;
    }


}

