package com.vssnake.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.WheelVerticalView;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;

public class ActivitySpinnerSelection extends Activity {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HOUR_MODE, NORMAL_MODE})
    public @interface NavigationMode {}

    public static final int HOUR_MODE = 1;
    public static final int NORMAL_MODE = 2;

    public static final String KEY_SELECT_MODE = "keyMode";
    public static final String KEY_NORMAL_MODE_SIZE = "keyNormalSize";
    public static final String KEY_TITLE = "keyTitle";

    LinearLayout container;
    Button btnSave;

    private static final int KEY_POSITION_WHEEL = 1;

    private int mode;
    private int numberSpinners;

    SparseArray<Integer> valuesWheels = new SparseArray<Integer>(4);

    public void setMode(@NavigationMode int mode){
        
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_selection);
        container = (LinearLayout)findViewById(R.id.SP_Container);

        mode = getIntent().getIntExtra(KEY_SELECT_MODE,NORMAL_MODE);

        btnSave = (Button) findViewById(R.id.APS_btnSave);


        switch (mode){
            case HOUR_MODE:
                addWheelTimeMode(container);
                break;
            case NORMAL_MODE:
                numberSpinners = getIntent().getIntExtra(KEY_NORMAL_MODE_SIZE,1);
                addWheel(container,numberSpinners);
                break;
        }
        String title = getIntent().getStringExtra(KEY_TITLE);
        setTitle(title);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                int result  = 0;
                switch (mode){
                    case HOUR_MODE:
                        //Algoritm to put the values of HourMode in seconds
                         result = ((valuesWheels.get(0,0) * 10 + valuesWheels.get(1,0
                                ))*60)
                                + valuesWheels.get(2,0) * 10 + valuesWheels.get(3,0);

                        break;
                    case NORMAL_MODE:
                        for(int i = numberSpinners; 0< i; i--){
                            result += valuesWheels.get(numberSpinners -i,0)*Math.pow(10,i-1);

                        }

                        break;
                }
                returnIntent.putExtra(KEY_SELECT_MODE,mode);
                returnIntent.putExtra("result",result);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

    }

    /**
     * Initializes spinnerwheel
     * @param id the spinnerwheel wheel Id
     */
    private void initWheel(int id) {
        AbstractWheel wheel = getWheel(id);
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
        wheel.setCurrentItem((int) (Math.random() * 10));
        // wheel.addChangingListener(changedListener);
        // wheel.addScrollingListener(scrolledListener);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }

    /**
     * Returns spinnerwheel by Id
     * @param id the spinnerwheel Id
     * @return the spinnerwheel with passed Id
     */
    private AbstractWheel getWheel(int id) {
        View C = findViewById(id);
        ViewGroup parent = (ViewGroup) C.getParent();
        int index = parent.indexOfChild(C);
        parent.removeView(C);
        antistatic.spinnerwheel.WheelVerticalView wheelVertical = new antistatic.spinnerwheel
                .WheelVerticalView(this);
        parent.addView(wheelVertical, index);
        return (AbstractWheel) wheelVertical;
    }

    /**
     * Add a N number of wheels consecutively with default construction
     * @param parent the parent of the wheels
     * @param countWheels number of wheels
     */
    private void addWheel(LinearLayout parent,int countWheels){
        for (int i=0;i<countWheels;i++){
            antistatic.spinnerwheel.WheelVerticalView wheelVertical = new antistatic.spinnerwheel
                    .WheelVerticalView(this);

            wheelVertical.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
            wheelVertical.setCurrentItem((int) (Math.random() * 10));

            wheelVertical.setTag(R.id.APS_btnSave, Integer.valueOf(i));
            wheelVertical.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                    Integer position = (Integer)wheel.getTag(R.id.APS_btnSave);
                    valuesWheels.append(position,newValue);
                }
            });
            // wheel.addChangingListener(changedListener);
            // wheel.addScrollingListener(scrolledListener);
            wheelVertical.setCyclic(true);
            wheelVertical.setInterpolator(new AnticipateOvershootInterpolator());
            wheelVertical.setMinimumWidth(120);
            wheelVertical.setCurrentItem(0);
            parent.addView(wheelVertical);
        }


    }

    /**
     * Add a 4 Wheels and a textView between EX. "00:00"
     * @param parent
     */
    private void addWheelTimeMode(LinearLayout parent){
        for (int i=0;i<4;i++){
            if (i == 2){
                TextView txtView = new TextView(this);
                txtView.setText(":");
                txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                txtView.setTextColor(Color.BLACK);
                txtView.setPadding(0,0,0,20);
               // Utils.getPixels(this, 30);
                parent.addView(txtView);
            }
            antistatic.spinnerwheel.WheelVerticalView wheelVertical = new antistatic.spinnerwheel
                    .WheelVerticalView(this);

            //The time format is base 6. A little algorithm to fix this
            if (i%2 != 0){
                wheelVertical.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
            }else{
                wheelVertical.setViewAdapter(new NumericWheelAdapter(this, 0, 5));
            }

            wheelVertical.setCurrentItem((int) (Math.random() * 10));

            wheelVertical.setTag(R.id.APS_btnSave, Integer.valueOf(i));
            wheelVertical.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                    Integer position = (Integer)wheel.getTag(R.id.APS_btnSave);
                    valuesWheels.append(position,newValue);
                }
            });


            wheelVertical.setCyclic(true);
            wheelVertical.setInterpolator(new AnticipateOvershootInterpolator());
            wheelVertical.setMinimumWidth(120);
            wheelVertical.setCurrentItem(0);
            parent.addView(wheelVertical);
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_spinner_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
