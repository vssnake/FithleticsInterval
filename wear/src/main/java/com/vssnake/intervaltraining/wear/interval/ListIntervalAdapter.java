package com.vssnake.intervaltraining.wear.interval;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.unai.intervaltraining.R;
import com.vssnake.intervaltraining.shared.interval.ListIntervalAdapterShared;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;

import java.util.ArrayList;

/**
 * Created by unai on 05/09/2014.
 */
public class ListIntervalAdapter extends ListIntervalAdapterShared{

    private static final String TAG = "ListIntervalAdapterWear";

    public ListIntervalAdapter(Context c, ArrayList<IntervalStaticData.ListIntervalData> d) {
        super(c, d);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListIntervalAdapterShared.ViewHolder holder;
        Log.v(TAG, "in getView for position " + position + ". convertView is " +
                ((convertView == null) ? "null" : "being recycled"));

        if (convertView == null){
            convertView = inflater.inflate(
                    R.layout.list_inteval_wear,null);

            convertViewCounter++;

            Log.v(TAG, convertViewCounter + " convertViews have been created");

            holder = new ListIntervalAdapterShared.ViewHolder();



            holder.imgIcon = (ImageView)convertView.
                    findViewById(R.id.list_intervals_icon);
            holder.txtName = (TextView) convertView.
                    findViewById(R.id.list_intervals_name);


            convertView.setTag(holder);

        }
        holder = (ListIntervalAdapterShared.ViewHolder) convertView.getTag();
        IntervalStaticData.ListIntervalData data = getItem(position);

        holder.txtName.setText(data.getName());
       // holder.txtDescription.setText(data.getDescription());
        holder.imgIcon.setImageResource(data.getIcon());

        return convertView;
    }
}
