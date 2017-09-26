package pt.simdea.guestlist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listaCompras;
    private ItemsDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        datasource = new ItemsDataSource(this);
        datasource.open();

        listaCompras = (ListView) findViewById(R.id.listacompras);
        ArrayList<Item> values = datasource.getNotItems();
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
                                        item.setBuyed(true);
                                        datasource.updateItem(item);
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
                datasource.updateItem(item);
                adapter.notifyDataSetChanged();
            }
        });
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listaCompras.setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void addElement(String name) {
        ListAdapter adapter = (ListAdapter) listaCompras.getAdapter();
        // save the new Item to the database
        if (!name.equals("")) {
            Item item = datasource.createItem(name);
            adapter.add(item);
        } else
            Toast.makeText(this, "Item vazio", Toast.LENGTH_LONG).show();
        adapter.notifyDataSetChanged();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
