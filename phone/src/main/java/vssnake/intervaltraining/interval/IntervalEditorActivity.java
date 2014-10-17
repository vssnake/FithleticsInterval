package vssnake.intervaltraining.interval;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.utils.StacData;
import com.vssnake.intervaltraining.shared.utils.Utils;
import com.vssnake.utils.ActivitySpinnerSelection;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.customFragments.InfoIntervalFragment;

public class IntervalEditorActivity extends Activity {

    TextView txtName;

    TextView txtRestTime;
    TextView txtEffortTime;
    TextView txtNumberIntervals;
    Button btnSave;


    //Codes for activityResult
    final int codeEffort = 1;
    final int codeRest = 2;
    final int codeIntervals = 3;

    public static final String KEY_NAME = "keyName";
    public static final String KEY_EFFORT = "keyEffort";
    public static final String KEY_REST = "keyRest";
    public static final String KEY_INTERVAL = "keyInterval";
    public static final String KEY_ID = "keyId";

    int timeEffort = 0, timeRest = 0,numberIntervals = 0;
    Long editID;

    LinearLayout linearEffort;
    LinearLayout linearRest;
    LinearLayout linearIntervals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_editor);

        txtName = (TextView) findViewById(R.id.interval_editor_name);
        txtRestTime = (TextView) findViewById(R.id.IE_txt_SecondsRest);
        txtEffortTime = (TextView) findViewById(R.id.IE_txt_SecondsEffort);
        txtNumberIntervals= (TextView) findViewById(R.id.IE_txt_NumberIntervals);

        linearEffort = (LinearLayout) findViewById(R.id.IE_Effort);
        linearRest = (LinearLayout) findViewById(R.id.IE_Rest);
        linearIntervals = (LinearLayout) findViewById(R.id.IE_Intervals);

        btnSave = (Button) findViewById(R.id.IE_btnSave);



        linearEffort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntervalEditorActivity.this,
                        ActivitySpinnerSelection.class);
                intent.putExtra(ActivitySpinnerSelection.KEY_SELECT_MODE,
                        ActivitySpinnerSelection.HOUR_MODE);
                intent.putExtra(ActivitySpinnerSelection.KEY_TITLE,
                        getString(R.string.IE_Title_TimeEffort));

                startActivityForResult(intent, codeEffort);
               // startActivity(intent);
            }
        });

        linearRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntervalEditorActivity.this,
                        ActivitySpinnerSelection.class);
                intent.putExtra(ActivitySpinnerSelection.KEY_SELECT_MODE,
                        ActivitySpinnerSelection.HOUR_MODE);
                intent.putExtra(ActivitySpinnerSelection.KEY_TITLE,
                        getString(R.string.IE_Title_TimeRest));

                startActivityForResult(intent, codeRest);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(KEY_ID,editID.intValue());
                    returnIntent.putExtra(KEY_NAME,txtName.getText().toString());
                    returnIntent.putExtra(KEY_EFFORT,timeEffort);
                    returnIntent.putExtra(KEY_REST,timeRest);
                    returnIntent.putExtra(KEY_INTERVAL,numberIntervals);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }

            }
        });

        linearIntervals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntervalEditorActivity.this,
                        ActivitySpinnerSelection.class);
                intent.putExtra(ActivitySpinnerSelection.KEY_SELECT_MODE,
                        ActivitySpinnerSelection.NORMAL_MODE);
                intent.putExtra(ActivitySpinnerSelection.KEY_NORMAL_MODE_SIZE,
                        2);
                intent.putExtra(ActivitySpinnerSelection.KEY_TITLE,
                        getString(R.string.IE_Title_NumberIntervals));

                startActivityForResult(intent, codeIntervals);
            }
        });

        //Check if Interval enter a new interval or a previous interval data and it performs some operations
        editID = getIntent().getLongExtra(InfoIntervalFragment.KEY_EDIT_INTERVAL_ID,-1);
        if(editID != -1){
            loadExistingID(editID);
        }
    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        if (resultCode == RESULT_OK){
            switch (requestCode){
                case codeEffort:
                    timeEffort = data.getIntExtra("result", 0);
                    txtEffortTime.setText(Utils.formatTime(timeEffort));
                    break;
                case codeIntervals:

                    numberIntervals = data.getIntExtra("result",0);
                    txtNumberIntervals.setText(Integer.toString(numberIntervals));
                    break;
                case codeRest:
                    timeRest = data.getIntExtra("result",0);
                    txtRestTime.setText(Utils.formatTime(timeRest));
                    break;
            }
        }

    }

    private boolean check(){
        boolean checked= true;
        String name = txtName.getText().toString();

        if (name.isEmpty()){
            txtName.setError("Name not should be blank");

            checked = false;
        }else{
            txtName.setError(null);
        }
        if (timeEffort == 0){

            txtEffortTime.setError("Effort Time not should be blank");
            checked = false;
        }else{
            txtEffortTime.setError(null);
        }
        if (timeRest == 0){

            txtRestTime.setError("Rest Time not should be blank");
            checked = false;
        }else{
            txtRestTime.setError(null);
        }
        if (numberIntervals == 0){
            txtNumberIntervals.setError("Number Intervals not should be a 0");
            checked = false;
        }else{
            txtNumberIntervals.setError(null);
        }
        return checked;

    }

    private void loadExistingID(Long id){
       IntervalStaticData.IntervalData data =  IntervalStaticData.intervalData.get(id.intValue());
        timeEffort = data.getmTimeDoing();
        timeRest = data.getmTimeResting();
        numberIntervals = data.getmTotalIntervals();
        txtName.setText(data.getmName());

        txtRestTime.setText(Utils.formatTime(timeRest));
        txtEffortTime.setText(Utils.formatTime(timeEffort));
        txtNumberIntervals.setText(numberIntervals + "");
    }
}
