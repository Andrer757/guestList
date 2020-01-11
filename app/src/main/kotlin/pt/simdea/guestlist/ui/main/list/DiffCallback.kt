package pt.simdea.guestlist.ui.main.list

import androidx.recyclerview.widget.DiffUtil
import pt.simdea.guestlist.db.model.Item

class DiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem.item == newItem.item && oldItem.counter == newItem.counter

}