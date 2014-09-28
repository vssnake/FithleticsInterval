package com.vssnake.intervaltraining.wear.interval;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.unai.intervaltraining.R;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.utils.Utils;

import java.util.List;

public class ListTrainActivity extends Activity {

    /*WatchViewStub mWatchViewStub;
    ImageView mIcon;
    TextView mName;
    TextView mDescription;
    Button mButton;
    */
    ListView listView;
    ListIntervalAdapter listIntervalAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_train);

       listView = (ListView) findViewById(R.id.listViewTraining);
       /* mWatchViewStub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        mWatchViewStub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mIcon = (ImageView) stub.findViewById(R.id.train_descrip_icon);
                mName = (TextView) stub.findViewById(R.id.train_descrip_name);
                mDescription = (TextView) stub.findViewById(R.id.train_descrip_description);
                mButton = (Button) stub.findViewById(R.id.train_descrip_button_start);

                mName.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mDescription.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mButton.setTypeface(Utils.getFontRoboto_black(getAssets()));


            }
        });*/

        listIntervalAdapter = new ListIntervalAdapter(this,
                IntervalStaticData.toList(this));

        listView.setAdapter(listIntervalAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object intervalData = parent.getItemAtPosition(position);
                if (intervalData!= null) {
                    long ID = ((IntervalStaticData.ListIntervalData) intervalData).getID();
                    Intent intent = new Intent(ListTrainActivity.this,
                            TrainDescriptionActivity.class);
                    intent.putExtra("trainID",ID);
                    startActivity(intent);

                }
            }
        });

    }
}
