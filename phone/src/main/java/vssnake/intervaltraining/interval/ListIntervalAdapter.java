package vssnake.intervaltraining.interval;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.model.IntervalStaticData;


/**
 * Created by unai on 26/08/2014.
 */
public class ListIntervalAdapter extends BaseAdapter {

    static class ViewHolder{
        TextView txtName;
        TextView txtDescription;
        ImageView imgIcon;


    }

    private static final String TAG = "ListIntervalAdapter";

    private static int convertViewCounter = 0;

    private ArrayList<IntervalStaticData.ListIntervalData> data;
    private LayoutInflater inflater = null;


    public ListIntervalAdapter(Context c, ArrayList<IntervalStaticData.ListIntervalData> d)
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.v(TAG,"in getView for position " + position + ". convertView is " +
                ((convertView== null) ? "null" : "being recycled"));

        if (convertView == null){
            convertView = inflater.inflate(R.layout.list_intervals,null);

            convertViewCounter++;

            Log.v(TAG, convertViewCounter + " convertViews have been created");

            holder = new ViewHolder();



            holder.imgIcon = (ImageView)convertView.
                    findViewById(R.id.list_intervals_icon);
            holder.txtName = (TextView) convertView.
                    findViewById(R.id.list_intervals_name);
            holder.txtDescription = (TextView) convertView.
                    findViewById(R.id.list_intervals_description);

            convertView.setTag(holder);

        }
            holder = (ViewHolder) convertView.getTag();
            IntervalStaticData.ListIntervalData data = getItem(position);
            holder.txtName.setText(data.getName());
            holder.txtDescription.setText(data.getDescription());
            holder.imgIcon.setImageResource(data.getIcon());

        return convertView;
    }
}
