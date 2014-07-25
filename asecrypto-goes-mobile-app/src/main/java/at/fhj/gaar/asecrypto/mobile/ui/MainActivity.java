package at.fhj.gaar.asecrypto.mobile.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import at.fhj.gaar.asecrypto.mobile.R;

public class MainActivity extends Activity implements NavigationDrawerCallable, SectionAttachable {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence currentScreenTitle;

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
    public void onTaskItemSelected(int taskId, String taskName) {
        Fragment currentTask = FragmentFactory.getFragment(taskId, taskName);

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, currentTask)
                .commit();
    }

    @Override
    public void onSectionAttached(String title) {
        currentScreenTitle = title;
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

}
