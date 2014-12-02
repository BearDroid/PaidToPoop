package com.beardroid.paid2poop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Max on 11/25/2014.
 */
public class PoopCardAdapter extends RecyclerView.Adapter<PoopCardAdapter.PoopCardHolder> {
    private List<PoopCard> PoopCardList;
    private Context mContext;

    public PoopCardAdapter(List<PoopCard> PoopCardList, Context context) {
        this.PoopCardList = PoopCardList;
        this.mContext = context;
    }

    @Override
    public PoopCardHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.poopcard, viewGroup, false);
        return new PoopCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PoopCardHolder holder, int position) {
        final PoopCard pc = PoopCardList.get(position);
        String amt = pc.getAmount();
        Double amtDbl = Double.parseDouble(amt);
        DecimalFormat moneyFormat = new DecimalFormat("0.00");
        amt = moneyFormat.format(amtDbl);
        amt = "$" + amt;
        holder.mAmount.setText(amt);
        holder.mTime.setText(pc.getTime());
        holder.mDate.setText(pc.getDate());
        holder.mRating.setText(pc.getRating());
        String rating = pc.getRating();
        if(rating.equals("Good")){
            holder.mRatingColor.setBackgroundColor(Color.parseColor("#2196F3")); //2980b9
            holder.mRating.setBackgroundColor(Color.parseColor("#1976D2"));
        } else if(rating.equals("Okay")){
            holder.mRatingColor.setBackgroundColor(Color.parseColor("#9C27B0")); //8e44ad
            holder.mRating.setBackgroundColor(Color.parseColor("#7B1FA2"));
        } else if(rating.equals("Bad")){
            holder.mRatingColor.setBackgroundColor(Color.parseColor("#F44336")); //e74c3c
            holder.mRating.setBackgroundColor(Color.parseColor("#D32F2F"));
        }

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, DetailActivity.class);
                myIntent.putExtra("Amount",pc.getAmount());
                myIntent.putExtra("Rating", pc.getRating());
                myIntent.putExtra("Date", pc.getDate());
                myIntent.putExtra("Time", pc.getTime());
                startActivity((MainActivity) mContext, myIntent, Bundle.EMPTY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PoopCardList.size();
    }

    public static class PoopCardHolder extends RecyclerView.ViewHolder{
        protected TextView mAmount;
        protected TextView mDate;
        protected TextView mTime;
        protected TextView mRating;
        protected CardView mLayout;
        protected RelativeLayout mRatingColor;
        protected TextView mSmiley;

        public PoopCardHolder(View v) {
            super(v);
            mAmount = (TextView) v.findViewById(R.id.money);
            mDate = (TextView) v.findViewById(R.id.date);
            mTime = (TextView) v.findViewById(R.id.time);
            mRating = (TextView) v.findViewById(R.id.smiley); //this should eventually change to an imageview I think.
            mRatingColor = (RelativeLayout) v.findViewById(R.id.rating);
            mLayout = (CardView) v.findViewById(R.id.card_view);
        }
    }

}
