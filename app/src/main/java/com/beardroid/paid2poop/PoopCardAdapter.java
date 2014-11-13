package com.beardroid.paid2poop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Max on 11/13/2014.
 */
public class PoopCardAdapter extends ArrayAdapter<PoopCard> {
    private final ArrayList<PoopCard> objects;


    public PoopCardAdapter(Context context, int textViewResourceId, ArrayList<PoopCard> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder view;

        if (rowView == null) {
            // Get a new instance of the row layout view
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.poopcard, null);

            // Hold the view objects in an object, that way the don't need to be "re-  finded"
            view = new ViewHolder();
            view.amount = (TextView) rowView.findViewById(R.id.money);
            view.date = (TextView) rowView.findViewById(R.id.date);
            view.time = (TextView) rowView.findViewById(R.id.time);

            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();
        }
        PoopCard item = objects.get(position);
        if (item != null) {
            double moneyDbl = item.getAmount();
            String moneyStr = String.valueOf(moneyDbl);
            view.amount.setText(moneyStr);
            view.date.setText(item.getDate());
            view.time.setText(item.getTime());

            return rowView;
        } else {
            return null;
        }


    }

    protected static class ViewHolder {
        protected TextView amount;
        protected TextView date;
        protected TextView time;
    }
}
