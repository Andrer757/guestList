package pt.simdea.guestlist.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pt.simdea.guestlist.Item
import pt.simdea.guestlist.R
import pt.simdea.guestlist.databinding.RowBinding

class ListAdapter(private val modelsArrayList: MutableList<Item>) : RecyclerView.Adapter<ListAdapter.RowViewHolder>() {

    override fun getItemCount() = modelsArrayList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder =  RowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false))

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val binding = holder.dataBinding ?: return
        binding.titleView.text = modelsArrayList[position].item
        binding.counterView.text = String.format("%d", modelsArrayList[position].getCounter())
    }

    fun getItem(position: Int) = modelsArrayList[position]

    /**
     * Procedure meant to insert a Generic Layout Implementation item on this adapter's data set.
     *
     * @param item a Generic Layout Implementation item.
     */
    fun add(item: Item) {
        val index = validateItemPosition(item)
        modelsArrayList.add(item)
        notifyItemInserted(index)
    }

    /**
     * Procedure meant to insert a list of Generic Layout Implementation item on this adapter's data set.
     *
     * @param items the list of Generic Layout Implementation items to be inserted.
     */
    fun add(items: List<Item>) {
        val index = validateLastItemPosition()
        modelsArrayList.addAll(items)
        notifyItemRangeChanged(index, items.size)
    }

    /**
     * Procedure meant to insert a Generic Layout Implementation item on this adapter's data set, at a target position.
     *
     * @param item     a Generic Layout Implementation item.
     * @param position the target position for this item to be inserted.
     * @throws IndexOutOfBoundsException if the position is out of range
     * (<tt>position &lt; 0 || position &gt; size()</tt>)
     */
    fun add(item: Item, position: Int) {
        modelsArrayList.add(position, item)
        notifyItemChanged(position)
    }

    /**
     * Procedure meant to remove a Generic Layout Implementation item on this adapter's data set, at a target position.
     *
     * @param position the target position for this item to be removed.
     */
    fun remove(position: Int) {
        if (modelsArrayList.isNotEmpty() && validateItemPosition(position)) {
            modelsArrayList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * Procedure meant to remove a list of Generic Layout Implementation item on this adapter's data set.
     *
     * @param items the list of Generic Layout Implementation items to be removed.
     */
    fun remove(items: List<Item>) {
        if (modelsArrayList.isNotEmpty() && items.isNotEmpty()) {
            val index = validateLastItemPosition()
            modelsArrayList.removeAll(items)
            notifyItemRangeRemoved(index, items.size)
        }
    }

    /**
     * Procedure meant to update a Generic Layout Implementation item present on this adapter's data set.
     *
     * @param item a Generic Layout Implementation item.
     */
    fun update(item: Item) {
        add(item)
    }

    /**
     * Procedure meant to swap this adapter's entire data set.
     *
     * @param items the list of Generic Layout Implementation items to be added.
     */
    fun swap(items: List<Item>) {
        if (items.isNotEmpty()) {
            modelsArrayList.clear()
            modelsArrayList.addAll(items)
            notifyDataSetChanged()
        }
    }

    /**
     * Procedure meant to perform an integrity check on a target index.
     *
     * @param index the target index.
     * @return whether the target index is in range on this adapter's data set.
     */
    private fun validateItemPosition(index: Int) = index in 1 until itemCount

    /**
     * Procedure meant to check the position of a target item on the data set.
     *
     * @param item the target Generic Layout Implementation item.
     * @return the position of a target item on the data set if it exists, else returns the next position to be filled.
     */
    private fun validateItemPosition(item: Item) = if (modelsArrayList.contains(item)) modelsArrayList.indexOf(item) else itemCount

    /**
     * Procedure meant to check the last known filled position on the data set.
     *
     * @return the last known filled position on the data set.
     */
    private fun validateLastItemPosition() = if (itemCount - 1 < 0) 0 else itemCount - 1

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataBinding: RowBinding? = DataBindingUtil.bind(itemView)
    }

}