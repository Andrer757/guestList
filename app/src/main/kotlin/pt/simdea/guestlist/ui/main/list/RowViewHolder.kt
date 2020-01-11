package pt.simdea.guestlist.ui.main.list

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pt.simdea.guestlist.databinding.RowBinding
import pt.simdea.guestlist.db.model.Item

class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal lateinit var item: Item

    fun bind(item: Item, upCount: (Item) -> Unit) {
        val dataBinding: RowBinding? = DataBindingUtil.bind(itemView)
        this.item = item
        dataBinding?.let { holder ->
            holder.root.setOnClickListener {
                upCount(item)
            }
            holder.titleView.text = item.item
            holder.counterView.text = String.format("%d", item.counter)
        }
    }

}
