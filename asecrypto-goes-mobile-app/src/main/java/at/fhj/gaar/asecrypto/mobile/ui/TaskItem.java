package at.fhj.gaar.asecrypto.mobile.ui;

/**
 * Represents a user-chooseable task.
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
}
