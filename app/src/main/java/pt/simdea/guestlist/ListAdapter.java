package pt.simdea.guestlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Item> {
    private final Context context;
    private final List<Item> modelsArrayList;

    public ListAdapter(Context context, List<Item> objects) {
        super(context, R.layout.row, objects);

        this.context = context;
        this.modelsArrayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = null;
        if (!modelsArrayList.get(position).getIsGroupHeader()) {
            rowView = inflater.inflate(R.layout.row, parent, false);

            // 3. Get icon,title & counter views from the rowView
            //ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
            TextView titleView = rowView.findViewById(R.id.textView1);
            TextView counterView = rowView.findViewById(R.id.item_counter);

            // 4. Set the text for textView
            //imgView.setImageResource(modelsArrayList.get(position).getIcon());
            titleView.setText(modelsArrayList.get(position).getItem());
            counterView.setText(String.format("%d", modelsArrayList.get(position).getCounter()));
        } else {
            rowView = inflater.inflate(R.layout.group_header_item, parent, false);
            TextView titleView = rowView.findViewById(R.id.header);
            titleView.setText(modelsArrayList.get(position).getItem());
        }

        return rowView;
    }
}