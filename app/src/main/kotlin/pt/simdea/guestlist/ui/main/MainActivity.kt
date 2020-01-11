package pt.simdea.guestlist.ui.main

import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.simdea.guestlist.R
import pt.simdea.guestlist.databinding.ActivityMainBinding
import pt.simdea.guestlist.db.model.Item
import pt.simdea.guestlist.ui.base.BaseActivity
import pt.simdea.guestlist.ui.main.list.ListAdapter
import pt.simdea.guestlist.ui.main.list.RowViewHolder
import pt.simdea.guestlist.ui.widgets.MaterialSearchView

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private val listAdapter: ListAdapter by lazy {
        ListAdapter(::onItemClick)
    }

    override fun layoutToInflate() = R.layout.activity_main

    override fun getViewModelClass() = MainViewModel::class.java

    override fun doOnCreated() {
        setSupportActionBar(dataBinding.toolbar)

        dataBinding.counter.text = viewModel.getCounter().toString()
        dataBinding.searchView.setVoiceSearch(false)
        dataBinding.searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                addElement(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })

        dataBinding.listacompras.setHasFixedSize(true)
        dataBinding.listacompras.setItemViewCacheSize(30)
        dataBinding.listacompras.isNestedScrollingEnabled = false
        dataBinding.listacompras.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        dataBinding.listacompras.adapter = listAdapter
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val holder = viewHolder as RowViewHolder

                if (dataBinding.listacompras.adapter!!.itemCount > 0) {
                    val item = holder.item
                    viewModel.deleteItem(item.id)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(dataBinding.listacompras) //set swipe to recycler view

        viewModel.itemsLiveData?.observe(this, Observer {
            listAdapter.setList(it)
            dataBinding.counter.text = viewModel.getCounter().toString()
        })

    }

    fun addElement(name: String) {
        // save the new Item to the database
        if (name != "") {
            viewModel.addItem(name)
        } else
            Toast.makeText(this, "Item vazio", Toast.LENGTH_LONG).show()
    }

    fun onItemClick(item: Item) {
        viewModel.updateItem(item.id)
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

}
