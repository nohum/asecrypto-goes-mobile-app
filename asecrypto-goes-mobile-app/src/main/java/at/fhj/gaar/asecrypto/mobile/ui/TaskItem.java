package at.fhj.gaar.asecrypto.mobile.ui;

/**
 * A dummy item representing a piece of content.
 */
public class TaskItem {

    private int id;

    private String title;

    public TaskItem(int id, final String title) {
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

    public void setTitle(String title) {
        this.title = title;
    }
}
