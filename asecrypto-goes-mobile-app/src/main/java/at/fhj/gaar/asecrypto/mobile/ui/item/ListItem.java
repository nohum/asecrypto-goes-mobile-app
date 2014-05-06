package at.fhj.gaar.asecrypto.mobile.ui.item;

/**
 * A dummy item representing a piece of content.
 */
public class ListItem {

    private int id;

    private String title;

    private String explanation;

    public ListItem(int id, String title, String explanation) {
        this.id = id;
        this.title = title;
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
