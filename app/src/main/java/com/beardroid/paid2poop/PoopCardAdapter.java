package com.beardroid.paid2poop;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Max on 11/25/2014.
 */
public class PoopCardAdapter extends RecyclerView.Adapter<PoopCardAdapter.PoopCardHolder> {
    private List<PoopCard> PoopCardList;

    public PoopCardAdapter(List<PoopCard> PoopCardList) {
        this.PoopCardList = PoopCardList;
    }

    @Override
    public PoopCardHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.poopcard, viewGroup, false);
        return new PoopCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PoopCardHolder holder, int position) {
        PoopCard pc = PoopCardList.get(position);
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
            holder.mRatingColor.setBackgroundColor(Color.parseColor("#2980b9"));
        } else if(rating.equals("Okay")){
            holder.mRatingColor.setBackgroundColor(Color.parseColor("#8e44ad"));
        } else if(rating.equals("Bad")){
            holder.mRatingColor.setBackgroundColor(Color.parseColor("#e74c3c"));
        }

    }

    @Override
    public int getItemCount() {
        return PoopCardList.size();
    }

    public static class PoopCardHolder extends RecyclerView.ViewHolder {
        protected TextView mAmount;
        protected TextView mDate;
        protected TextView mTime;
        protected TextView mRating;
        protected RelativeLayout mRatingColor;

        public PoopCardHolder(View v) {
            super(v);
            mAmount = (TextView) v.findViewById(R.id.money);
            mDate = (TextView) v.findViewById(R.id.date);
            mTime = (TextView) v.findViewById(R.id.time);
            mRating = (TextView) v.findViewById(R.id.smiley); //this should eventually change to an imageview I think.
            mRatingColor = (RelativeLayout) v.findViewById(R.id.rating);
        }
    }
}
