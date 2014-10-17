package com.vssnake.intervaltraining.wear.interval;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.unai.intervaltraining.R;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.utils.StacData;
import com.vssnake.intervaltraining.shared.utils.Utils;
import com.vssnake.intervaltraining.wear.IntervalActivity;


/**
 * Created by unai on 05/09/2014.
 */
public class TrainDescriptionActivity extends Activity {

    WatchViewStub mWatchViewStub;
    TextView mName;
    TextView mDescription;
    Button mButton;

    long ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWatchViewStub = new WatchViewStub(this);

      //  mWatchViewStub = (WatchViewStub) findViewById(R.id.activity_train_description_view_stub);

        mWatchViewStub.setRoundLayout(R.layout.activity_train_description_round);
        mWatchViewStub.setRectLayout(R.layout.activity_train_description_rect);

        mWatchViewStub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mName = (TextView) stub.findViewById(R.id.train_descrip_name);
                mDescription = (TextView) stub.findViewById(R.id.train_descrip_description);
                mButton = (Button) stub.findViewById(R.id.train_descrip_button_start);

                mName.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mDescription.setTypeface(Utils.getFontRoboto_black(getAssets()));
                mButton.setTypeface(Utils.getFontRoboto_black(getAssets()));

                ID =TrainDescriptionActivity.this.getIntent().getLongExtra("trainID",0);
                IntervalStaticData.IntervalData data = IntervalStaticData.intervalData.get((int)ID);
              //  mIcon.setImageResource(data.getIcon());
                mName.setText(data.getmName());


                Resources resources = getResources();

                mDescription.setText(
                    String.format(resources.getString(R.string.interval_effort),data.getmTimeDoing())+
                    String.format(resources.getString(R.string.interval_rest),data.getmTimeResting())+
                    String.format(resources.getString(R.string.interval_rounds),
                            data.getmTotalIntervals()));

                mButton.setOnClickListener(TrainDescriptionActivity.this.buttonListener);


            }
        });

        setContentView(mWatchViewStub);

    }
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(TrainDescriptionActivity.this,IntervalActivity.class);
            intent.putExtra(StacData.TRAIN_ID_KEY,ID);
            startActivity(intent);
        }
    };
}
