package vssnake.intervaltraining.interval;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vssnake.intervaltraining.shared.interval.ListIntervalAdapterShared;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;

import java.util.ArrayList;

import vssnake.intervaltraining.R;

/**
 * Created by unai on 05/09/2014.
 */
public class ListIntervalAdapter extends ListIntervalAdapterShared {
    public ListIntervalAdapter(Context c, ArrayList<IntervalStaticData.ListIntervalData> d) {
        super(c, d);
    }

    private static final String TAG = "ListIntervalAdapterPhone";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Log.v(TAG, "in getView for position " + position + ". convertView is " +
                ((convertView == null) ? "null" : "being recycled"));

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
