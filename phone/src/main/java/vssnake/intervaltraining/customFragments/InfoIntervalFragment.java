package vssnake.intervaltraining.customFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.Toast;

import com.vssnake.intervaltraining.shared.utils.Utils;

import uk.me.lewisdeane.ldialogs.BaseDialog;
import uk.me.lewisdeane.ldialogs.CustomDialog;
import uk.me.lewisdeane.ldialogs.CustomListDialog;
import vssnake.intervaltraining.R;
import vssnake.intervaltraining.interval.IntervalEditorActivity;
import vssnake.intervaltraining.interval.ListIntervalAdapter;
import vssnake.intervaltraining.utils.StacData;

import com.vssnake.intervaltraining.shared.interval.ListIntervalAdapterShared;
import com.vssnake.intervaltraining.shared.model.IntervalStaticData;
import com.vssnake.intervaltraining.shared.wearable.WearableService;


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

    CustomListDialog dialog;
    Long mDialogItemId;
   // PopupMenu menu;


    final int codeAddInterval = 4000;
    final int codeEditInterval = 4001;

    private final static int MENU_EDIT_INTERVAL = 0;
    private final static int MENU_DELETE_INTERVAL = 1;


    public final static String KEY_EDIT_INTERVAL_ID = "keyEdit";

    public interface onInfoIntervalFragmentListener{
        void trainSelected(long idTrain);
        boolean trainStarted();
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

        initializeMenu(getActivity());

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




        mLVIntervals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object intervalData = parent.getItemAtPosition(position);
                if (mInfoFragmentListener.trainStarted()){
                    showTrainisStartedError(getActivity(),R.string.train_started_error_description);
                }else{
                    if (intervalData != null) {

                        mDialogItemId = ((IntervalStaticData.ListIntervalData) intervalData).getID();
                        dialog.show();
                    }
                }

                return  true;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInfoFragmentListener.trainStarted()){
                    showTrainisStartedError(getActivity(),R.string.train_started_error_description);
                }else {
                    Intent intent = new Intent(getActivity(), IntervalEditorActivity.class);
                    startActivityForResult(intent, codeAddInterval);
                }

            }
        });

        return view;

    }




    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK){
            if (mInfoFragmentListener.trainStarted()) {
                showTrainisStartedError(getActivity(),R.string.train_started_error_description);
                return;
            }
            ListIntervalAdapterShared adapter;
            String name;
            switch ( requestCode){
                case codeAddInterval:
                    Log.d(TAG,"onActivityResult codeAddInterval executed");
                    name = data.getStringExtra(IntervalEditorActivity.KEY_NAME.toString());
                    IntervalStaticData.addInterval(
                            name,
                            data.getIntExtra(IntervalEditorActivity.KEY_EFFORT,0),
                            data.getIntExtra(IntervalEditorActivity.KEY_REST,0),
                            data.getIntExtra(IntervalEditorActivity.KEY_INTERVAL,0));

                    adapter = new ListIntervalAdapter(getActivity(),
                            IntervalStaticData.toList(getActivity().getApplicationContext()));

                    //Send data to wearables
                    WearableService.setDataMap(WearableService.INTERVAL_DATA,
                            IntervalStaticData.WearableUtils.convertToDataMap());
                    mLVIntervals.setAdapter(adapter);
                    break;
                case codeEditInterval:
                    Log.d(TAG,"onActivityResult codeEditInterval executed");
                    name = data.getStringExtra(IntervalEditorActivity.KEY_NAME.toString());
                    int ID = data.getIntExtra(IntervalEditorActivity.KEY_ID, -1);
                    if (ID == -1){
                        Log.e(TAG,"onActivityResult codeEditInterval executed and id is -1");
                    }else{
                        IntervalStaticData.replaceInterval(ID,
                                name,
                                data.getIntExtra(IntervalEditorActivity.KEY_EFFORT,0),
                                data.getIntExtra(IntervalEditorActivity.KEY_REST,0),
                                data.getIntExtra(IntervalEditorActivity.KEY_INTERVAL,0));

                        //Send data to wearables
                        WearableService.setDataMap(WearableService.INTERVAL_DATA,
                                IntervalStaticData.WearableUtils.convertToDataMap());

                        adapter = new ListIntervalAdapter(getActivity(),
                                IntervalStaticData.toList(getActivity().getApplicationContext()));
                        mLVIntervals.setAdapter(adapter);

                        mInfoFragmentListener.trainSelected(0);
                    }

                    break;
            }
        }else{
            Log.d(TAG,"onActivityResult resultCode is RESULT_FAIL");
        }

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

    private void showTrainisStartedError(Context context,int text){
        Resources res = getActivity().getResources();


        Toast toast = Toast.makeText(context,text,
                Toast.LENGTH_LONG);
        toast.show();
    }

    private void initializeMenu(Context context){
        Resources res = getActivity().getResources();
        // Create the builder with required paramaters - Context, Title, Positive Text
        CustomListDialog.Builder builder = new CustomListDialog.Builder(context,
                res.getString(R.string.interval_Custom_Operation),new String[]{
                res.getString(R.string.interval_custom_op_edit),
                res.getString(R.string.interval_custom_op_delete)
        });

        builder.darkTheme(true);
        builder.typeface(Utils.getFontRoboto_regular(getActivity().getAssets()));
        builder.titleAlignment(BaseDialog.Alignment.CENTER);
        builder.itemAlignment(BaseDialog.Alignment.CENTER);
        builder.titleColor(res.getColor(R.color.orange));
        builder.itemColor(res.getColor(R.color.startInterval));
        builder.titleTextSize(30);
        builder.itemTextSize(22);

        dialog = builder.build();



        dialog.setListClickListener(new CustomListDialog.ListClickListener() {
            @Override
            public void onListItemSelected(int i, String[] strings, String s) {
                if(MENU_EDIT_INTERVAL == i){
                    Intent intent = new Intent(getActivity(), IntervalEditorActivity.class);
                    intent.putExtra(KEY_EDIT_INTERVAL_ID,mDialogItemId);
                    startActivityForResult(intent, codeEditInterval);
                }else if (MENU_DELETE_INTERVAL == i){
                    if (!IntervalStaticData.deleteInterval(mDialogItemId.intValue())){
                        showTrainisStartedError(getActivity(),R.string.only_1_interval_remaining);
                    }

                    ListIntervalAdapterShared adapter;
                    adapter = new ListIntervalAdapter(getActivity(),
                            IntervalStaticData.toList(getActivity().getApplicationContext()));
                    mLVIntervals.setAdapter(adapter);


                    mInfoFragmentListener.trainSelected(0);

                    //Send data to wearables
                    WearableService.setDataMap(WearableService.INTERVAL_DATA,
                            IntervalStaticData.WearableUtils.convertToDataMap());


                }
            }
        });



       /* menu = new PopupMenu(context);
        Resources res = getActivity().getResources();
        menu.setHeaderTitle(res.getString(R.string.interval_Custom_Operation));
        menu.
        menu.add(MENU_EDIT_INTERVAL, R.string.interval_custom_op_edit);
        menu.add(MENU_DELETE_INTERVAL,R.string.interval_custom_op_delete);

        menu.setOnItemSelectedListener(new PopupMenu.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MenuItem menuItem) {

            }
        });*/

    }
}
