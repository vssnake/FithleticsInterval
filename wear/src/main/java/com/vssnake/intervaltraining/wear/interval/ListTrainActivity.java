package com.vssnake.intervaltraining.wear.interval;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.unai.intervaltraining.R;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;

public class ListTrainActivity extends Activity {

    WatchViewStub mWatchViewStub;

    ListView listView;
    ListIntervalAdapter listIntervalAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_train);

        mWatchViewStub = (WatchViewStub) findViewById(R.id.activity_list_train_view_stub);
        mWatchViewStub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                listView = (ListView) stub.findViewById(R.id.listViewTraining);
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
        });

        IntervalStaticData.initIntervalData(this);
        listIntervalAdapter = new ListIntervalAdapter(this,
                IntervalStaticData.toList(this));



    }
}
