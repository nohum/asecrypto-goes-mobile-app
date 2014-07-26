package at.fhj.gaar.asecrypto.mobile.ui.navigation;

/**
 * Callbacks interface that all activities using this fragment must implement.
 */
public interface NavigationDrawerCallable {

    /**
     * Called when an task item of the navigation drawer is selected.
     */
    void onTaskItemSelected(int taskId, String taskName);

}
