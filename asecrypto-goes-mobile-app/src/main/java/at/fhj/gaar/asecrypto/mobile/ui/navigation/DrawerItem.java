package at.fhj.gaar.asecrypto.mobile.ui.navigation;

/**
 * Represents a user-chooseable task.
 */
public class DrawerItem {

    private int id;

    private String title;

    public DrawerItem(int id, final String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
