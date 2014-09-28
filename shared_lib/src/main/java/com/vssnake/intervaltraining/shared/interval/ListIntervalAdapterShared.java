package com.vssnake.intervaltraining.shared.interval;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



import com.vssnake.intervaltraining.shared.model.IntervalStaticData;


/**
 * Created by unai on 26/08/2014.
 */
public abstract  class ListIntervalAdapterShared extends BaseAdapter {

    public static class ViewHolder{
        public TextView txtName;
        public TextView txtDescription;
        public ImageView imgIcon;


    }

    private static final String TAG = "ListIntervalAdapterShared";

    protected static int convertViewCounter = 0;

    protected  ArrayList<IntervalStaticData.ListIntervalData> data;
    protected LayoutInflater inflater = null;


    public ListIntervalAdapterShared(Context c, ArrayList<IntervalStaticData.ListIntervalData> d)
    {
        Log.v(TAG, "Building ListIntervalAdapter");

        this.data = d;
        inflater = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        Log.v(TAG, "in getCount()");
        return data.size();
    }

    @Override
    public IntervalStaticData.ListIntervalData getItem(int position) {
        Log.v(TAG,"in getItem() for position" + position);
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.v(TAG,"in getItemID() for position" + position);

        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);


}
