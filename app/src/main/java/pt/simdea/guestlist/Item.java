package pt.simdea.guestlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import pt.simdea.gmlrva.lib.IGenericRecyclerViewLayout;
import pt.simdea.gmlrva.lib.IViewHolder;

@Entity
public class Item implements IGenericRecyclerViewLayout<Item.RowViewHolder> {
    @Id
    private long id;
    private int icon;
    private String item;
    private int counter;
    private boolean buyed;

    public Item() {
        this(-1, "", 0);
    }

    public Item(String title, int counter) {
        this(-1,title,counter);
    }

    public Item(int icon, String title, int counter) {
        this.icon = icon;
        this.item = title;
        this.counter = counter;
        buyed = false;
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

    @NonNull
    @Override
    public RowViewHolder createViewHolder(@NonNull ViewGroup parent) {
        return new RowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false));
    }

    @Override
    public void setElements(@NonNull RowViewHolder holder) {
        holder.titleView.setText(item);
        holder.counterView.setText(String.format("%d", counter));
    }

    @NonNull
    @Override
    public Object getTag() {
        return item;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder implements IViewHolder {

        @BindView(R.id.textView1)
        protected TextView titleView;
        @BindView(R.id.item_counter)
        protected TextView counterView;

        public RowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void recycle() {

        }
    }
}
