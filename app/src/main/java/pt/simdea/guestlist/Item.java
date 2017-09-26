package pt.simdea.guestlist;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Item {
    @Id
    private long id;
    private int icon;
    private String item;
    private int counter;
    private boolean buyed;

    private boolean isGroupHeader = false;

    public Item(String title, int counter) {
        this(-1,title,counter);
        //isGroupHeader = true;
    }

    public Item(int icon, String title, int counter) {
        super();
        this.icon = icon;
        this.item = title;
        this.counter = counter;
        buyed = false;
    }

    public boolean getIsGroupHeader() {
        return isGroupHeader;
    }

    public int getIcon() {
        return icon;
    }

    public long getCounter() {
        return counter;
    }

    public void addCount() {
        counter++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean getBuyed() {
        return buyed;
    }

    public void setBuyed(boolean buyed) {
        this.buyed = buyed;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return item;
    }
}
