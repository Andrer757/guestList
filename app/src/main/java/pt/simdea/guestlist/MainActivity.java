package pt.simdea.guestlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

public class MainActivity extends AppCompatActivity {

    private ListView listaCompras;
    private Box<Item> itemsBox;
    private Query<Item> itemsQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BoxStore boxStore = ((App) getApplication()).getBoxStore();
        itemsBox = boxStore.boxFor(Item.class);

        // query all notes, sorted a-z by their text (http://greenrobot.org/objectbox/documentation/queries/)
        itemsQuery = itemsBox.query().order(Item_.id).build();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listaCompras = findViewById(R.id.listacompras);
        List<Item> values = itemsQuery.find();
        final ListAdapter adapter = new ListAdapter(this, values);
        listaCompras.setAdapter(adapter);
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listaCompras,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    if (listaCompras.getAdapter().getCount() > 0) {
                                        Item item = adapter.getItem(position);
                                        itemsBox.remove(item);
                                        adapter.remove(item);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        listaCompras.setOnTouchListener(touchListener);
        listaCompras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Item item = adapter.getItem(position);
                item.addCount();
                itemsBox.put(item);
                adapter.notifyDataSetChanged();
            }
        });
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listaCompras.setOnScrollListener(touchListener.makeScrollListener());
    }

    public void addElement(@NonNull String name) {
        ListAdapter adapter = (ListAdapter) listaCompras.getAdapter();
        // save the new Item to the database
        if (!name.equals("")) {
            Item item = new Item(name, 0);
            itemsBox.put(item);
            adapter.add(item);
        } else
            Toast.makeText(this, "Item vazio", Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();

    }
}
