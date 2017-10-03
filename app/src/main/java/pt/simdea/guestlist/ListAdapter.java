package pt.simdea.guestlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.RowViewHolder> {
    private final List<Item> modelsArrayList;

    public ListAdapter(List<Item> objects) {
        this.modelsArrayList = objects;
    }

    @Override
    public ListAdapter.RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false));
    }

    @Override
    public void onBindViewHolder(ListAdapter.RowViewHolder holder, int position) {
        holder.titleView.setText(modelsArrayList.get(position).getItem());
        holder.counterView.setText(String.format("%d", modelsArrayList.get(position).getCounter()));
    }

    @Override
    public int getItemCount() {
        return modelsArrayList.size();
    }

    public Item getItem(int position) {
        return modelsArrayList.get(position);
    }

    /**
     * Procedure meant to insert a Generic Layout Implementation item on this adapter's data set.
     *
     * @param item a Generic Layout Implementation item.
     */
    public void add(@NonNull final Item item) {
        final int index = validateItemPosition(item);
        modelsArrayList.add(item);
        notifyItemInserted(index);
    }

    /**
     * Procedure meant to insert a list of Generic Layout Implementation item on this adapter's data set.
     *
     * @param items the list of Generic Layout Implementation items to be inserted.
     */
    public void add(@NonNull final List<Item> items) {
        final int index = validateLastItemPosition();
        modelsArrayList.addAll(items);
        notifyItemRangeChanged(index, items.size());
    }

    /**
     * Procedure meant to insert a Generic Layout Implementation item on this adapter's data set, at a target position.
     *
     * @param item     a Generic Layout Implementation item.
     * @param position the target position for this item to be inserted.
     * @throws IndexOutOfBoundsException if the position is out of range
     *                                   (<tt>position &lt; 0 || position &gt; size()</tt>)
     */
    public void add(@NonNull final Item item, final int position) {
        modelsArrayList.add(position, item);
        notifyItemChanged(position);
    }

    /**
     * Procedure meant to remove a Generic Layout Implementation item on this adapter's data set, at a target position.
     *
     * @param position the target position for this item to be removed.
     */
    public void remove(final int position) {
        if (!modelsArrayList.isEmpty() && validateItemPosition(position)) {
            modelsArrayList.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Procedure meant to remove a list of Generic Layout Implementation item on this adapter's data set.
     *
     * @param items the list of Generic Layout Implementation items to be removed.
     */
    public void remove(@NonNull final List<Item> items) {
        if (!modelsArrayList.isEmpty() && !items.isEmpty()) {
            final int index = validateLastItemPosition();
            modelsArrayList.removeAll(items);
            notifyItemRangeRemoved(index, items.size());
        }
    }

    /**
     * Procedure meant to update a Generic Layout Implementation item present on this adapter's data set.
     *
     * @param item a Generic Layout Implementation item.
     */
    public void update(@NonNull final Item item) {
        add(item);
    }

    /**
     * Procedure meant to swap this adapter's entire data set.
     *
     * @param items the list of Generic Layout Implementation items to be added.
     */
    public void swap(@NonNull final List<Item> items) {
        if (!items.isEmpty()) {
            modelsArrayList.clear();
            modelsArrayList.addAll(items);
            notifyDataSetChanged();
        }
    }

    /**
     * Procedure meant to perform an integrity check on a target index.
     *
     * @param index the target index.
     * @return whether the target index is in range on this adapter's data set.
     */
    private boolean validateItemPosition(final int index) {
        return index > 0 && index < getItemCount();
    }

    /**
     * Procedure meant to check the position of a target item on the data set.
     *
     * @param item the target Generic Layout Implementation item.
     * @return the position of a target item on the data set if it exists, else returns the next position to be filled.
     */
    private int validateItemPosition(@NonNull final Item item) {
        return modelsArrayList.contains(item) ? modelsArrayList.indexOf(item) : getItemCount();
    }

    /**
     * Procedure meant to check the last known filled position on the data set.
     *
     * @return the last known filled position on the data set.
     */
    private int validateLastItemPosition() {
        return getItemCount() - 1 < 0 ? 0 : getItemCount() - 1;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView1)
        protected TextView titleView;
        @BindView(R.id.item_counter)
        protected TextView counterView;

        public RowViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}