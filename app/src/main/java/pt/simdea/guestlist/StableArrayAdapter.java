package pt.simdea.guestlist;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class StableArrayAdapter extends ArrayAdapter<Item> {
    HashMap<Item, Integer> mIdMap = new HashMap<Item, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        Item item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public void add(Item object) {
        mIdMap.put(object, mIdMap.size());
        super.add(object);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}