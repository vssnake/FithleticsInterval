package vssnake.intervaltraining.training;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import vssnake.intervaltraining.customFragments.InfoIntervalFragment;
import vssnake.intervaltraining.R;
import vssnake.intervaltraining.customFragments.ChronometerFragment;
import vssnake.intervaltraining.main.Main_Activity;


public abstract class TabataTrainingBase_Fragment extends Fragment{


    OnFragmentInteractionListener mListener;

    Button mStartCountDownButton;

    ChronometerFragment mChronometerFragment;

    InfoIntervalFragment mInfoIntervalFragment;



    FrameLayout FirstFrame;
    FrameLayout SecondFrame;

    Main_Activity main_Activity;

    Intent intent;
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
        if (getArguments() != null) {

        }
        main_Activity =(Main_Activity) getActivity();
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
    public void onDestroy(){
        super.onDestroy();
       // getActivity().stopService(new Intent(getActivity(),Tabata_Service.class));
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
       /* android.app.FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Create the Fragments and  push into the corresponds FrameLayouts
        mChronometerFragment = mChronometerFragment.newInstance("","");
        mInfoIntervalFragment = InfoIntervalFragment.newInstance("","");

        this.getChildFragmentManager().beginTransaction()
                .replace(R.id.intervalFragment_FirstFrame, mInfoIntervalFragment)
                .commit();
        this.getChildFragmentManager().beginTransaction()
                .replace(R.id.intervalFragment_SecondFrame, mChronometerFragment)
                .commit();





        View view = inflater.inflate(R.layout.fragment_training_tabata, container, false);

        FirstFrame = (FrameLayout)view.findViewById(R.id.intervalFragment_FirstFrame);
        SecondFrame = (FrameLayout)view.findViewById(R.id.intervalFragment_SecondFrame);





        mStartCountDownButton = (Button) view.findViewById(R.id.training_button);
        //mChronometerFragment = (ChronometerFragment) getFragmentManager().findFragmentById(R.id.tabataChronometer_Fragment);




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



}