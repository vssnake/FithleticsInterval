package vssnake.intervaltraining.interval;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.vssnake.intervaltraining.shared.interval.TrainingBase_Service;
import com.vssnake.utils.SlidingMenu;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.customFragments.ChronometerFragment;
import vssnake.intervaltraining.customFragments.InfoIntervalFragment;
import com.vssnake.intervaltraining.shared.utils.StacData;


public abstract class TabataTrainingBase_Fragment extends android.support.v4.app.Fragment
implements  InfoIntervalFragment.onInfoIntervalFragmentListener{


    OnFragmentInteractionListener mListener;

    View mIntervalClickView;


    Resources mRes;

    ChronometerFragment mChronometerFragment;

    InfoIntervalFragment mInfoIntervalFragment;

    RelativeLayout mParent;

    FrameLayout mFirstFrame;
    FrameLayout mSecondFrame;

    FragmentActivity main_Activity;

    Intent intent;

    View mShadowFrame2;

    CheckBox mCBVibration;
    CheckBox mCBSound;

    SharedPreferences mSharedPreferences;

    SlidingMenu mSliding;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment TabataTrainingBase_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabataTraining_Fragment newInstance() {
        TabataTraining_Fragment fragment = new TabataTraining_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main_Activity = getActivity();
       // intent = new Intent(main_Activity,Interval_Service.class);

        mSharedPreferences = getActivity().getSharedPreferences(StacData.BASIC_CONFIG_PREFS,0);

        intent = new Intent(getActivity(),IntervalPhoneService.class);


    }
    @Override
    public void onPause(){
       super.onPause();
    }
    @Override
    public void onResume(){

        super.onResume();

    }




    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }


    private int _xDelta;
    private int _yDelta;

    private int basicHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Create the Fragments and  push into the corresponds FrameLayouts
        mChronometerFragment = mChronometerFragment.newInstance("","");
        mInfoIntervalFragment = InfoIntervalFragment.newInstance(this);


        View view = inflater.inflate(R.layout.fragment_training_tabata, container, false);

        this.getChildFragmentManager().beginTransaction()
                .replace(R.id.intervalFragment_FirstFrame, mInfoIntervalFragment)
                .commit();
        this.getChildFragmentManager().beginTransaction()
                .replace(R.id.intervalFragment_SecondFrame, mChronometerFragment)
                .commit();



        mFirstFrame = (FrameLayout)view.findViewById(R.id.intervalFragment_FirstFrame);
        mSecondFrame = (FrameLayout)view.findViewById(R.id.intervalFragment_SecondFrame);

        basicHeight = mFirstFrame.getHeight();
        mParent = (RelativeLayout)view.findViewById(R.id.intervalFragment_Parent);


        mIntervalClickView = (View) view.findViewById(R.id.intervalFragment_clickView);

        mShadowFrame2 = (View) view.findViewById(R.id.intervalFragment_shadowFrame2);

        mCBSound = (CheckBox) view.findViewById(R.id.intervalFragment_checkBox_ActiveSound);
        mCBVibration = (CheckBox) view.findViewById(R.id.intervalFragment_checkBox_ActiveVibration);


        mCBVibration.setChecked(mSharedPreferences.getBoolean(StacData.PREFS_VIBRATION_KEY,true));
        mCBSound.setChecked(mSharedPreferences.getBoolean(StacData.PREFS_SOUND_KEY,true));

        mCBSound.setOnClickListener(mCBActiveSoundListener);
        mCBVibration.setOnClickListener(mCBActiveVibrationListener);


        //Set the  top menu. The fragment is not yet inflated and need work around :-(
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mFirstFrame.getHeight() == 0){
                    handler.postDelayed(this,10);

                }else{


                   mSliding = SlidingMenu.newInstance(getActivity(),mFirstFrame,
                            SlidingMenu.MaxSliding.ALL_FRAME,0,
                            (int)TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_DIP,80,mRes.getDisplayMetrics()));
                    mSliding.setEvents(new SlidingMenu.SlidingEvents() {
                        @Override
                        public void onMovementFinished(boolean slideOpen) {
                            mInfoIntervalFragment.menuHide(slideOpen);
                        }
                    });

                }

            }
        },10);





        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;

            mRes = getResources();



        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        //Found the parent activity but not the nested fragment, therefore we need
        //redirection for it.
        mInfoIntervalFragment.onActivityResult(requestCode,resultCode,data);

    }

    protected  TrainingBase_Service.stateFlag checkIsServiceisRunning(){
       return  TrainingBase_Service.getStateService();
    }

    private View.OnClickListener mCBActiveSoundListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            if (((CheckBox)v).isChecked()){
                 editor.putBoolean(StacData.PREFS_SOUND_KEY,true);
            }else{
                editor.putBoolean(StacData.PREFS_SOUND_KEY,false);
            }
            editor.commit();
        }
    };
    private View.OnClickListener mCBActiveVibrationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            if (((CheckBox)v).isChecked()){
                editor.putBoolean(StacData.PREFS_VIBRATION_KEY,true);
            }else{
                editor.putBoolean(StacData.PREFS_VIBRATION_KEY,false);
            }
            editor.commit();
        }
    };






    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }







}
