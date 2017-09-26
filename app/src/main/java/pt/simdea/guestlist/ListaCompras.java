package pt.simdea.guestlist;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaCompras extends ListView {
    private ItemsDataSource datasource;

    public ListaCompras(Context context) {
        super(context);
        datasource = new ItemsDataSource(context);
        datasource.open();

        ArrayList<Item> values = datasource.getAllItems();

        ListAdapter adapter = new ListAdapter(context, values);
        setAdapter(adapter);
    }

    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ListAdapter adapter = (ListAdapter) getAdapter();
        Item Item = null;
        /*switch (view.getId()) {
            case R.id.add:
                String[] Items = new String[] { "Cool", "Very nice", "Hate it" };
                int nextInt = new Random().nextInt(3);
                // save the new Item to the database
                Item = datasource.createItem(Items[nextInt]);
                adapter.add(Item);
                break;
            case R.id.delete:
                if (getAdapter().getCount() > 0) {
                    Item = (Item) getAdapter().getItem(0);
                    datasource.deleteItem(Item);
                    adapter.remove(Item);
                }
                break;
        }*/
        adapter.notifyDataSetChanged();
    }

    //@Override
    public void onResume() {
        datasource.open();
        //super.onResume();
    }

    //@Override
    public void onPause() {
        datasource.close();
        //super.onPause();
    }

}
