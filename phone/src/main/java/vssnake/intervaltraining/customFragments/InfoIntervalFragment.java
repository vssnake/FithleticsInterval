package vssnake.intervaltraining.customFragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vssnake.intervaltraining.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoIntervalFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class InfoIntervalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView mInfoIntervalTitle;
    TextView mInfoIntervalRound;
    TextView mInfoIntervalMode;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoIntervalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoIntervalFragment newInstance(String param1, String param2) {
        InfoIntervalFragment fragment = new InfoIntervalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public InfoIntervalFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_interval, container, false);

        // Inflate the layout for this fragment
        //Reference the textViews to modify the data in runtime
        mInfoIntervalTitle = (TextView)view.findViewById
                (R.id.infoInterval_Title_TextView);
        mInfoIntervalRound =  (TextView)view.findViewById
                (R.id.infoInterval_Rounds_TextView);
        mInfoIntervalMode= (TextView)view.findViewById
                (R.id.infoInterval_Mode_TextView);

        return view;
    }





    @Override
    public void onDetach() {
        super.onDetach();
    }



    public void changeTitle(String title){
        if (isAdded()){
            mInfoIntervalTitle.setText(title);
        }

    }
    public void changeRound(int currentRound, int totalRounds){
        if (isAdded()){
            mInfoIntervalRound.setText(currentRound + "/"+ totalRounds);
        }

    }
    public void changeMode(String mode){
        if (isAdded()){
            mInfoIntervalMode.setText(mode);
        }

    }
}
