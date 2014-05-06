package at.fhj.gaar.asecrypto.mobile.ui.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListContent {

    /**
     * An array of sample (dummy) items.
     */
    private static List<ListItem> ITEMS = new ArrayList<ListItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    private static Map<Integer, ListItem> ITEM_MAP = new HashMap<Integer, ListItem>();

    static {
        addItem(new ListItem(1, "Number counter", "Lab1_Task1"));
        addItem(new ListItem(2, "Euclid iterative and recursive", "Lab1_Task2"));
        addItem(new ListItem(3, "Speed of Euclid's algorithm", "Lab1_Task3"));
        addItem(new ListItem(4, "Bezout iterative and recursive", "Lab1_Task4"));
        addItem(new ListItem(5, "Speed of slow exponentation", "Lab2_Task1"));
        addItem(new ListItem(6, "Fast exponentation and comparison", "Lab2_Task2"));
        addItem(new ListItem(7, "Fermat test", "Lab3_Task1"));
        addItem(new ListItem(8, "Miller-Rabin test", "Lab3_Task2"));
        addItem(new ListItem(9, "RSA and the chinese remainder", "Exercise 15"));
        addItem(new ListItem(10, "Primitive root finder", "Exercise 18"));
    }

    private static void addItem(ListItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }


    public static List<ListItem> getItems() {
        return ITEMS;
    }

    public static Map<Integer, ListItem> getItemMap() {
        return ITEM_MAP;
    }

}
