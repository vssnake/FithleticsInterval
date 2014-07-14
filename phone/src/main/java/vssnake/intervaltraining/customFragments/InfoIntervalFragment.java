package vssnake.intervaltraining.customFragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.utils.Utils;


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

    TextView mInfoIntervalRoundText;
    TextView mInfoIntervalModeText;

    View mInfoIntervalMoveView;

    public interface onInfoIntervalFragmentListener{
        void moveInfoIntervalFragment(float x, float y);
    }

    onInfoIntervalFragmentListener mInfoFragmentListener;

    private InfoIntervalFragment(onInfoIntervalFragmentListener infoFragmentListener){
        mInfoFragmentListener = infoFragmentListener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param infoFragmentListener Parameter 1.
     * @return A new instance of fragment InfoIntervalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoIntervalFragment newInstance(onInfoIntervalFragmentListener infoFragmentListener) {
        InfoIntervalFragment fragment = new InfoIntervalFragment(infoFragmentListener);

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

    private int _xDelta;
    private int _yDelta;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info_interval, container, false);

        // Inflate the layout for this fragment
        //Reference the textViews to modify the data in runtime
        mInfoIntervalTitle = (TextView) view.findViewById
                (R.id.infoInterval_Title_TextView);
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


        /*
        mInfoIntervalMoveView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        _xDelta = X - lParams.leftMargin;
                        _yDelta = Y - lParams.topMargin;
                        Log.i("InfoIntervalMove",  "Action Down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                        v.setLayoutParams(layoutParams);
                        if (mInfoFragmentListener != null) {
                           /// Log.i("InfoIntervalMove", event.getX() + " " + event.getY());
                            mInfoFragmentListener.moveInfoIntervalFragment(X - _xDelta, Y - _yDelta);
                        }
                        break;
                    default:
                }


                //v.invalidate();
                return true;
            }
        });*/
        mInfoIntervalTitle.setTypeface(Utils.getFontRoboto_black(getActivity().getAssets()));
        mInfoIntervalModeText.setTypeface(Utils.getFontRoboto_regular(getActivity().getAssets()));
        mInfoIntervalRoundText.setTypeface(Utils.getFontRoboto_regular(getActivity().getAssets()));


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
