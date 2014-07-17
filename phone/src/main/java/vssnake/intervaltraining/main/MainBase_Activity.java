package vssnake.intervaltraining.main;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.TextView;

import vssnake.intervaltraining.R;
import vssnake.intervaltraining.interval.TabataTraining_Fragment;
import vssnake.intervaltraining.interval.TabataTrainingBase_Fragment;
import vssnake.intervaltraining.services.GoogleApiService;


public abstract class MainBase_Activity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, TabataTrainingBase_Fragment.OnFragmentInteractionListener {

    /**
     *
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    public  static final String FRAGMENT_KEY = "FRAGMENT";
    public static final int TABATA_FRAGMENT = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();




        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Check if a notification launch a Key to open a fragment
        Intent i = getIntent();
        int fragmentKey = i.getIntExtra(MainBase_Activity.FRAGMENT_KEY,-1);

        if (fragmentKey > -1){
            onNavigationDrawerItemSelected(fragmentKey);
        }


    }



    protected void onDestroy(){
        super.onDestroy();
      Log.i("DestroyMAin", "Destroy Main activity");
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent e){
        if (keyCode == KeyEvent.KEYCODE_MENU){
                if (mNavigationDrawerFragment.isDrawerOpen()) {
                    restoreActionBar();//Activate the default action bar title
                }
                mNavigationDrawerFragment.openDrawer();

        }
        return super.onKeyDown(keyCode,e);
    }

    //region Fragments
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment;
        switch (position){
            case 2:
                fragment = TabataTraining_Fragment.newInstance();
                break;
            default:
                fragment =  PlaceholderFragment.newInstance(position+1);
                break;
        }
        Log.i("BackStack",getFragmentManager().getBackStackEntryCount()+ "");
      /*  if (getFragmentManager().getBackStackEntryCount() != 0){
            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-1);
        }*/

       // if (fragmentManager.getBackStackEntryAt(getFragmentManager().getBackStackEntryCount()-1) != fragment){
            fragmentManager.beginTransaction()

                    .setCustomAnimations(
                            R.animator.card_flip_right_in,R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in,R.animator.card_flip_left_out)
                    .replace(R.id.container, fragment)
                    .commit();
        //}

    }

    /**
     * Choose the name of the title depends of the number os section is attached
     * @param number
     */
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
        }
    }

    //endregion

    //region Menus


    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.welcome_, menu);
            restoreActionBar();
            return true;
        }
        return false;
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
        return false;
    }

    //endregion


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        TextView section_Number;
        Button mTestButton;

        Integer _section_Number = 0;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome_, container, false);
            section_Number = (TextView)rootView.findViewById(R.id.section_label);

            section_Number.setText(_section_Number.toString());
             section_Number = (Button)rootView.findViewById(R.id.TestingButton);
            section_Number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message msg = Message.obtain(null, 0, 0, 0);
                    try {
                        mService.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
            });
            final Intent intent = new Intent(getActivity(),GoogleApiService.class);
            Thread t = new Thread(){
                public void run(){
                    getActivity().startService(intent);
                    getActivity().bindService(intent,mConnection, Context.BIND_ABOVE_CLIENT);
                }
            };
           t.start();

            return rootView;
        }
        Messenger mService = null;

        private ServiceConnection mConnection  = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }
        };

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);

            //Call to set the name of the section
            ((MainBase_Activity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

            _section_Number = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

}
