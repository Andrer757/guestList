package pt.simdea.guestlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import pt.simdea.gmlrva.lib.IGenericRecyclerViewLayout
import pt.simdea.gmlrva.lib.IViewHolder
import pt.simdea.guestlist.databinding.RowBinding

@Entity
class Item @JvmOverloads constructor(internal val icon: Int = -1, internal var item: String? = "", private var counter: Int = 0) : IGenericRecyclerViewLayout<Item.RowViewHolder> {
    @Id
    public var id: Long = 0
    internal var buyed: Boolean = false

    constructor(title: String, counter: Int) : this(-1, title, counter)

    fun getCounter(): Long = counter.toLong()

    fun addCount() {
        counter++
    }

    override fun toString(): String = item!!

    override fun createViewHolder(parent: ViewGroup): RowViewHolder = RowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false))

    override fun setElements(holder: RowViewHolder) {
        val binding = holder.dataBinding ?: return

        binding.titleView.text = item
        binding.counterView.text = String.format("%d", counter)
    }

    override fun getTag(): Any = item!!

    override fun getViewType(): Int = 0

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), IViewHolder {
        val dataBinding: RowBinding? = DataBindingUtil.bind(itemView)

        override fun recycle() { }
    }

    fun getItem() = item
    fun getBuyed() = buyed
    fun getIcon() = icon
}
