package vssnake.intervaltraining.customFragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vssnake.intervaltraining.shared.utils.Utils;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.interval.IntervalEditorActivity;
import vssnake.intervaltraining.interval.ListIntervalAdapter;

import com.vssnake.intervaltraining.shared.interval.ListIntervalAdapterShared;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoIntervalFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class InfoIntervalFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "InfoIntervalFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    TextView mInfoIntervalRound;
    TextView mInfoIntervalMode;

    TextView mInfoIntervalRoundText;
    TextView mInfoIntervalModeText;

    View mInfoIntervalMoveView;

    ListView mLVIntervals;

    ImageView mArrowImage;

    Button btnAdd;

    public interface onInfoIntervalFragmentListener{
        void trainSelected(long idTrain);
    }

    onInfoIntervalFragmentListener mInfoFragmentListener;








    public InfoIntervalFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InfoIntervalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoIntervalFragment newInstance(onInfoIntervalFragmentListener parentListener) {
        InfoIntervalFragment fragment = new InfoIntervalFragment();
        fragment.mInfoFragmentListener = parentListener;
        return fragment;
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

        mInfoIntervalRound = (TextView) view.findViewById
                (R.id.infoInterval_Rounds_TextView);
        mInfoIntervalMode = (TextView) view.findViewById
                 (R.id.infoInterval_Mode_TextView);
        mInfoIntervalMoveView = (View) view.findViewById
                (R.id.infoInterval_move_View);
        mInfoIntervalRoundText = (TextView) view.findViewById(
                R.id.infoInterval_Rounds_Text);
        mInfoIntervalModeText = (TextView) view.findViewById(
                R.id.infoInterval_Mode_Text);


        mLVIntervals = (ListView) view.findViewById
                (R.id.infoInterval_list_name);

        btnAdd = (Button) view.findViewById(R.id.infoInterval_AddButton);

        mArrowImage = (ImageView) view.findViewById(R.id.infoInterval_tick_image);


        mInfoIntervalModeText.setTypeface(Utils.getFontRoboto_regular(getActivity().getAssets()));
        mInfoIntervalRoundText.setTypeface(Utils.getFontRoboto_regular(getActivity().getAssets()));


        ListIntervalAdapterShared adapter = new ListIntervalAdapter(getActivity(),
                IntervalStaticData.toList(getActivity().getApplicationContext()));
        mLVIntervals.setAdapter(adapter);


        mLVIntervals.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object intervalData = parent.getItemAtPosition(position);
                if (intervalData!= null){
                    long ID = ((IntervalStaticData.ListIntervalData)intervalData).getID();
                    if (mInfoFragmentListener != null){
                        mInfoFragmentListener.trainSelected(ID);
                        Log.d(TAG,"on mLVIntervals on setOnclickListener | ID-> " + ID);
                    }else{
                        Log.e(TAG,"on mLVIntervals on setOnclickListener | mInfoFragmentListener " +
                                "null");
                    }


                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IntervalEditorActivity.class);
                getActivity().startActivityForResult(intent,0);
            }
        });

        return view;

    }




    @Override
    public void onDetach() {
        super.onDetach();
    }



    public void changeTitle(String title){
        if (isAdded()){
          //  mInfoIntervalTitle.setText(title);
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

    public void menuHide(boolean menuOpen){


            if (menuOpen){
                mArrowImage.setRotation(180);
            }else{
                mArrowImage.setRotation(360);
            }

    }
}
