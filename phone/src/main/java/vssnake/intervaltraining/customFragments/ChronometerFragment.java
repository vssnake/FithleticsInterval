package vssnake.intervaltraining.customFragments;



import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChronometerFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ChronometerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView mIntervalTime;
    private TextView mTotalTime;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Typeface font;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChronometerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChronometerFragment newInstance(String param1, String param2) {
        ChronometerFragment fragment = new ChronometerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ChronometerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chronometer, container, false);

       font = Typeface.createFromAsset(getActivity().getAssets(),"digitaldream.ttf");

        mIntervalTime = (TextView)view.findViewById(R.id.chronometer_TextView);
        mTotalTime = (TextView)view.findViewById(R.id.chronometerTotal_TextView);


        changeFontsAndSize();

        return view;
    }



    /**
     * Customieze the textViws of the Chronometer
     */
    private void changeFontsAndSize(){

        mIntervalTime.setTextSize(50);
        mIntervalTime.setTextColor(Color.RED);
        mIntervalTime.setTypeface(font);

        mTotalTime.setTextSize(40);
        mTotalTime.setTextColor(Color.RED);
        mTotalTime.setTypeface(font);
    }

    public void changeTime(Long secondsTotal,Long secondsInterval){
        if (isAdded()){
            //Reformat and convert of milliseconds to seconds
            secondsTotal = secondsTotal/1000;
            secondsInterval = secondsInterval/1000;

            mIntervalTime.setText(Utils.formatIntervalTime(secondsInterval));

            mTotalTime.setText(Utils.formatTotalIntervalTime(secondsTotal));
        }


    }
}
