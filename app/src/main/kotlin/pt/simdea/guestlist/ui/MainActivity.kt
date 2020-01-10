package pt.simdea.guestlist

import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.objectbox.Box
import io.objectbox.query.Query
import pt.simdea.gmlrva.lib.GenericMultipleLayoutAdapter
import pt.simdea.guestlist.databinding.ActivityMainBinding



class MainActivity : ActivityBase<ActivityMainBinding>(), RecyclerItemClickListener.OnItemClickListener {

    private var itemsBox: Box<Item>? = null
    private var itemsQuery: Query<Item>? = null

    override fun layoutToInflate(): Int = R.layout.activity_main

    override fun doOnCreated() {
        setSupportActionBar(dataBinding.toolbar)
        val boxStore = (application as App).boxStore
        itemsBox = boxStore!!.boxFor(Item::class.java)

        itemsQuery = itemsBox!!.query().order(Item_.id).build()

        dataBinding.counter.text = getCounter(itemsQuery).toString()

        dataBinding.searchView.setVoiceSearch(false)
        dataBinding.searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                addElement(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })

        val linearLayoutManager = LinearLayoutManager(this)
        dataBinding.listacompras.setHasFixedSize(true)
        dataBinding.listacompras.setItemViewCacheSize(30)
        dataBinding.listacompras.isNestedScrollingEnabled = false
        dataBinding.listacompras.layoutManager = linearLayoutManager
        dataBinding.listacompras.adapter = GenericMultipleLayoutAdapter(itemsQuery!!.find(), this, false)
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition //get position which is swipe

                if (dataBinding.listacompras.adapter!!.itemCount > 0) {
                    val adapter = dataBinding.listacompras.adapter as GenericMultipleLayoutAdapter
                    val item = adapter.get(position) as Item?
                    if (item != null) {
                        adapter.remove(item)
                        itemsBox!!.remove(item)
                        dataBinding.counter.text = getCounter(itemsQuery).toString()
                    }
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(dataBinding.listacompras) //set swipe to recycler view
        dataBinding.listacompras.addOnItemTouchListener(RecyclerItemClickListener(this, this))
    }

    fun addElement(name: String) {
        // save the new Item to the database
        if (name != "") {
            val item = Item(name, 1)
            val adapter = dataBinding.listacompras.adapter as GenericMultipleLayoutAdapter
            itemsBox!!.put(item)
            adapter.add(item)
            adapter.notifyDataSetChanged()
            dataBinding.counter.text = getCounter(itemsQuery).toString()
        } else
            Toast.makeText(this, "Item vazio", Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(view: View, position: Int) {
        val adapter = dataBinding.listacompras.adapter as GenericMultipleLayoutAdapter
        val item = adapter.get(position) as Item?
        item!!.addCount()
        itemsBox!!.put(item)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val item = menu.findItem(R.id.add)
        dataBinding.searchView.setMenuItem(item)

        return true
    }

    /**
     * {@inheritDoc}
     */
    override fun onBackPressed() {
        if (dataBinding.searchView.isSearchOpen) {
            dataBinding.searchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    private fun getCounter(itemsBox: Query<Item>?): Long {
        return itemsBox?.property(Item_.counter)?.sum() ?: 0
    }
}
