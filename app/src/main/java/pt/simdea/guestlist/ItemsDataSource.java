package pt.simdea.guestlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ItemsDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_ITEM, DatabaseHelper.COLUMN_COUNT, DatabaseHelper.COLUMN_BUYED };

	public ItemsDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Item createItem(String Item) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.COLUMN_ITEM, Item);
		values.put(DatabaseHelper.COLUMN_COUNT, 1);
		long insertId = database.insert(DatabaseHelper.TABLE_ITEMS, null, values);
		Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS,
				allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Item newItem = cursorToItem(cursor);
		cursor.close();
		return newItem;
	}

    public void updateItem(Item item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ITEM, item.getItem());
        values.put(DatabaseHelper.COLUMN_COUNT, Integer.parseInt(item.getCounter()));
        values.put(DatabaseHelper.COLUMN_BUYED, item.isBuyed()?1:0);
        database.update(DatabaseHelper.TABLE_ITEMS, values, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

	public void deleteItem(Item Item) {
		long id = Item.getId();
		System.out.println("Item deleted with id: " + id);
		database.delete(DatabaseHelper.TABLE_ITEMS, DatabaseHelper.COLUMN_ID + " = " + id, null);
	}

	public ArrayList<Item> getAllItems() {
		ArrayList<Item> Items = new ArrayList<Item>();

		Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Item Item = cursorToItem(cursor);
			Items.add(Item);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return Items;
	}

    public ArrayList<Item> getBuyedItems() {
        ArrayList<Item> Items = new ArrayList<Item>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, allColumns, DatabaseHelper.COLUMN_BUYED + " = 1", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item Item = cursorToItem(cursor);
            Items.add(Item);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Items;
    }

    public ArrayList<Item> getNotItems() {
        ArrayList<Item> Items = new ArrayList<Item>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, allColumns,  DatabaseHelper.COLUMN_BUYED + " = 0", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item Item = cursorToItem(cursor);
            Items.add(Item);
            cursor.moveToNext();
        }

        cursor.close();
        return Items;
    }

    private Item cursorToItem(Cursor cursor) {
        Item Item = new Item(cursor.getString(1), cursor.getInt(2));
        Item.setId(cursor.getLong(0));
        Item.setBuyed(cursor.getInt(2)==1);
        return Item;
    }
}