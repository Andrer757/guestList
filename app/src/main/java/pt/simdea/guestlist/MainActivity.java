package pt.simdea.guestlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import pt.simdea.gmlrva.lib.GenericMultipleLayoutAdapter;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    @BindView(R.id.listacompras)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.search_view)
    protected MaterialSearchView mSearchView;
    private Box<Item> itemsBox;
    private Query<Item> itemsQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        itemsBox = boxStore.boxFor(Item.class);

        itemsQuery = itemsBox.query().order(Item_.id).build();

        mSearchView.setVoiceSearch(false);
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addElement(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(30);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new GenericMultipleLayoutAdapter(itemsQuery.find(), this, false));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (mRecyclerView.getAdapter().getItemCount() > 0) {
                    GenericMultipleLayoutAdapter adapter = (GenericMultipleLayoutAdapter) mRecyclerView.getAdapter();
                    Item item = (Item) adapter.get(position);
                    if (item != null) {
                        adapter.remove(item);
                        itemsBox.remove(item);
                    }
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(mRecyclerView); //set swipe to recycler view
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    public void addElement(@NonNull String name) {
        // save the new Item to the database
        if (!name.equals("")) {
            Item item = new Item(name, 1);
            GenericMultipleLayoutAdapter adapter = (GenericMultipleLayoutAdapter) mRecyclerView.getAdapter();
            itemsBox.put(item);
            adapter.add(item);
            adapter.notifyDataSetChanged();
        } else
            Toast.makeText(this, "Item vazio", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        GenericMultipleLayoutAdapter adapter = (GenericMultipleLayoutAdapter) mRecyclerView.getAdapter();
        Item item = (Item) adapter.get(position);
        item.addCount();
        itemsBox.put(item);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.add);
        mSearchView.setMenuItem(item);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
