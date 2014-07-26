package at.fhj.gaar.asecrypto.mobile.ui.navigation;


import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import at.fhj.gaar.asecrypto.mobile.R;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallable drawerCallback;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle drawerToggle;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private View fragmentContainerView;

    private int currentSelectedPosition = 0;
    private boolean isDataFromSavedInstanceState;
    private boolean hasUserLearnedAboutDrawer;

    private DrawerItem[] drawerItems;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawerItems();

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        hasUserLearnedAboutDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            currentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            isDataFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(currentSelectedPosition);
    }

    private void initDrawerItems() {
        drawerItems = new DrawerItem[] {
                new DrawerItem(DrawerItemIdentifiers.TASK_NUMBER_COUNTER, getString(R.string.task_number_counter)),
                new DrawerItem(DrawerItemIdentifiers.TASK_EUCLID, getString(R.string.task_euclid)),
                new DrawerItem(DrawerItemIdentifiers.TASK_BEZOUT, getString(R.string.task_bezout)),
                new DrawerItem(DrawerItemIdentifiers.TASK_SLOW_EXPONENTATION, getString(R.string.task_slow_exponentation_speed)),
                new DrawerItem(DrawerItemIdentifiers.TASK_FAST_EXPONENTATION, getString(R.string.task_fast_exponentation)),
                new DrawerItem(DrawerItemIdentifiers.TASK_FERMAT_TEST, getString(R.string.task_fermat_test)),
                new DrawerItem(DrawerItemIdentifiers.TASK_CARMICHAEL_NUMBERS, getString(R.string.task_carmichael)),
                new DrawerItem(DrawerItemIdentifiers.TASK_MILLER_RABIN_TEST, getString(R.string.task_miller_rabin_test)),
                new DrawerItem(DrawerItemIdentifiers.TASK_RSA_CHINESE_REMAINDER, getString(R.string.task_rsa_chinese_remainder)),
                new DrawerItem(DrawerItemIdentifiers.TASK_PRIMITIVE_ROOTS, getString(R.string.task_primitive_roots))
        };
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Indicate that this fragment would like to influence the set of actions in the action bar.
        // Only used to show the app name.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        drawerListView = (ListView) inflater.inflate(
                R.layout.navigation_drawer, container, false);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        drawerListView.setAdapter(new ArrayAdapter<DrawerItem>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                drawerItems));
        drawerListView.setItemChecked(currentSelectedPosition, true);
        return drawerListView;
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(fragmentContainerView);
    }

    public void toggleDrawer() {
        if (isDrawerOpen()) {
            drawerLayout.closeDrawer(fragmentContainerView);
            return;
        }

        drawerLayout.openDrawer(fragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        fragmentContainerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        drawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                NavigationDrawerFragment.this.drawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!hasUserLearnedAboutDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    hasUserLearnedAboutDrawer = true;

                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!hasUserLearnedAboutDrawer && !isDataFromSavedInstanceState) {
            this.drawerLayout.openDrawer(fragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        this.drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

        this.drawerLayout.setDrawerListener(drawerToggle);
    }

    private void selectItem(int position) {
        currentSelectedPosition = position;
        if (drawerListView != null) {
            drawerListView.setItemChecked(position, true);
        }

        if (drawerLayout != null) {
            drawerLayout.closeDrawer(fragmentContainerView);
        }

        if (drawerCallback != null) {
            // TODO: range check of array element index before access
            drawerCallback.onTaskItemSelected(drawerItems[position].getId(),
                    drawerItems[position].getTitle());
        }
    }

    public void openItem(int position) {
        // TODO check if valid value (of DrawerItemIdentifiers)
        selectItem(position - 1);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            drawerCallback = (NavigationDrawerCallable) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        drawerCallback = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Forward the new configuration the drawer toggle component.
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (drawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        //noinspection ConstantConditions
        return getActivity().getActionBar();
    }

}
