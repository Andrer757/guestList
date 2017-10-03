package pt.simdea.guestlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

public class MainActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    @BindView(R.id.listacompras)
    protected RecyclerView listaCompras;
    private Box<Item> itemsBox;
    private Query<Item> itemsQuery;
    private ListAdapter adapter;

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

        List<Item> values = itemsQuery.find();
        adapter = new ListAdapter(values);
        listaCompras.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (listaCompras.getAdapter().getItemCount() > 0) {
                    Item item = adapter.getItem(position);
                    itemsBox.remove(item);
                    adapter.remove(position);
                }
                adapter.notifyDataSetChanged();
            }
        });
        itemTouchHelper.attachToRecyclerView(listaCompras); //set swipe to recylcerview
        listaCompras.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    @OnClick(R.id.fab)
    public void clickFab() {
        addElement("Tstejhadshjdçhsadçhldsafs");
    }

    public void addElement(@NonNull String name) {
        // save the new Item to the database
        if (!name.equals("")) {
            Item item = new Item(name, 0);
            itemsBox.put(item);
            adapter.add(item);
        } else
            Toast.makeText(this, "Item vazio", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Item item = adapter.getItem(position);
        item.addCount();
        itemsBox.put(item);
        adapter.notifyDataSetChanged();
    }
}
