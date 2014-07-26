package at.fhj.gaar.asecrypto.mobile.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import at.fhj.gaar.asecrypto.mobile.R;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.BaseFragment;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.fermat.FermatTestFragment;
import at.fhj.gaar.asecrypto.mobile.ui.apptasks.millerrabin.MillerRabinTestFragment;
import at.fhj.gaar.asecrypto.mobile.ui.navigation.DrawerItemIdentifiers;
import at.fhj.gaar.asecrypto.mobile.ui.navigation.NavigationDrawerCallable;
import at.fhj.gaar.asecrypto.mobile.ui.navigation.NavigationDrawerFragment;

public class MainActivity extends Activity implements NavigationDrawerCallable, SectionAttachable {

    /**
     * Externally specified default fragment to open (by Intent).
     */
    public static final String ARG_INTENT_DEFAULT_FRAGMENT = "fragment_to_open";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence currentScreenTitle;

    private int expectedFragmentTaskId;

    private Object fragmentParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        currentScreenTitle = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            int defaultItemNumber = intent.getIntExtra(ARG_INTENT_DEFAULT_FRAGMENT, 0);

            if (defaultItemNumber > 0) {
                navigationDrawerFragment.openItem(defaultItemNumber);
            }
        }
    }

    @Override
    public void onTaskItemSelected(int taskId, String taskName) {
        Fragment currentTask = FragmentFactory.getFragment(taskId, taskName);

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, currentTask)
                .commit();

        if (expectedFragmentTaskId == taskId
                && expectedFragmentTaskId == DrawerItemIdentifiers.TASK_FERMAT_TEST) {
            // set default number for Fermat test - given by Carmichael number generator
            ((FermatTestFragment) currentTask).setConcreteTestNumber((String) fragmentParameter);
        } else if (expectedFragmentTaskId == taskId
                && expectedFragmentTaskId == DrawerItemIdentifiers.TASK_MILLER_RABIN_TEST) {
            // set default number for Miller-Rabin test - given by Carmichael number generator
            ((MillerRabinTestFragment) currentTask)
                    .setConcreteTestNumber((String) fragmentParameter);
        }

        // reset
        expectedFragmentTaskId = 0;
        fragmentParameter = null;
    }

    @Override
    public void onSectionAttached(String title) {
        currentScreenTitle = title;

        if (getActionBar() == null) {
            return;
        }

        getActionBar().setTitle(title);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar == null) {
            return;
        }

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(currentScreenTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // open navigation drawer if the user clicks on the app icon button
        if (item.getItemId() == android.R.id.home) {
            navigationDrawerFragment.toggleDrawer();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openFermatTest(String number) {
        // is given to the Fragment in onTaskItemSelected
        fragmentParameter = number;
        expectedFragmentTaskId = DrawerItemIdentifiers.TASK_FERMAT_TEST;
        // after the fragment has been openen, onTaskItemSelected is called
        navigationDrawerFragment.openItem(DrawerItemIdentifiers.TASK_FERMAT_TEST);
    }

    public void openMillerRabinTest(String number) {
        // is given to the Fragment in onTaskItemSelected
        fragmentParameter = number;
        expectedFragmentTaskId = DrawerItemIdentifiers.TASK_MILLER_RABIN_TEST;
        // after the fragment has been openen, onTaskItemSelected is called
        navigationDrawerFragment.openItem(DrawerItemIdentifiers.TASK_MILLER_RABIN_TEST);
    }
}
