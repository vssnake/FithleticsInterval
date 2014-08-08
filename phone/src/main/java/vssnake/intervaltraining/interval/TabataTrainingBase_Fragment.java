package vssnake.intervaltraining.interval;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.customFragments.ChronometerFragment;
import vssnake.intervaltraining.customFragments.InfoIntervalFragment;
import vssnake.intervaltraining.main.Main_Activity;


public abstract class TabataTrainingBase_Fragment extends android.support.v4.app.Fragment
        implements InfoIntervalFragment.onInfoIntervalFragmentListener{


    OnFragmentInteractionListener mListener;

    View mIntervalClickView;



    ChronometerFragment mChronometerFragment;

    InfoIntervalFragment mInfoIntervalFragment;

    RelativeLayout mParent;

    FrameLayout mFirstFrame;
    FrameLayout mSecondFrame;

    FragmentActivity main_Activity;

    Intent intent;

    View mShadowFrame2;
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
        intent = new Intent(main_Activity,Interval_Service.class);


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Create the Fragments and  push into the corresponds FrameLayouts
        mChronometerFragment = mChronometerFragment.newInstance("","");
        mInfoIntervalFragment = InfoIntervalFragment.newInstance(this);

        this.getChildFragmentManager().beginTransaction()
                .replace(R.id.intervalFragment_FirstFrame, mInfoIntervalFragment)
                .commit();
        this.getChildFragmentManager().beginTransaction()
                .replace(R.id.intervalFragment_SecondFrame, mChronometerFragment)
                .commit();

        View view = inflater.inflate(R.layout.fragment_training_tabata, container, false);

        mFirstFrame = (FrameLayout)view.findViewById(R.id.intervalFragment_FirstFrame);
        mSecondFrame = (FrameLayout)view.findViewById(R.id.intervalFragment_SecondFrame);


        mParent = (RelativeLayout)view.findViewById(R.id.intervalFragment_Parent);


        mIntervalClickView = (View) view.findViewById(R.id.intervalFragment_clickView);

        mShadowFrame2 = (View) view.findViewById(R.id.intervalFragment_shadowFrame2);


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

    protected  TrainingBase_Service.stateFlag checkIsServiceisRunning(){
       return  TrainingBase_Service.getStateService();
    }




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

    int anterior = 0;

    @Override
    public void moveInfoIntervalFragment(float x, float y) {

        ViewGroup.MarginLayoutParams layoutParams =  (ViewGroup.MarginLayoutParams)mFirstFrame.getLayoutParams();
        Log.i("Margin", y +" " + anterior);
        layoutParams.topMargin += (int)y - anterior;
        anterior = (int)y;
        mParent.invalidate();

    }



}
