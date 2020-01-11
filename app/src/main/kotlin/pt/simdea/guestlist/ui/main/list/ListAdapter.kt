package pt.simdea.guestlist.ui.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import pt.simdea.guestlist.R
import pt.simdea.guestlist.db.model.Item

class ListAdapter(private val upCount: (Item) -> Unit = {}) : RecyclerView.Adapter<RowViewHolder>() {

    private val adapterDiff = DiffCallback()
    private val mDiffer = AsyncListDiffer(this, adapterDiff)

    fun setList(list: List<Item>) {
        mDiffer.submitList(ArrayList(list)) //creating a new list avoids problems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false))

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val category = mDiffer.currentList[position]
        holder.bind(category, upCount)
    }

    override fun getItemCount() = mDiffer.currentList.size

}