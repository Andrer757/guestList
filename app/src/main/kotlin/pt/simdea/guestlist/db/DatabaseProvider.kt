package pt.simdea.guestlist.db

import io.objectbox.Box
import io.objectbox.BoxStore
import pt.simdea.guestlist.db.model.Item
import pt.simdea.guestlist.db.model.Item_
import javax.inject.Inject

class DatabaseProvider @Inject constructor(boxStore: BoxStore) {

    private val itemsBox: Box<Item> = boxStore.boxFor(Item::class.java)

    fun saveItem(name: String) {
        itemsBox.put(Item(item = name, counter = 1))
    }

    fun updateCount(id: Long) {
        val item = itemsBox.query().equal(Item_.id, id).build().findFirst()
        item?.let {
            item.counter++
            itemsBox.put(item)
        }
    }

    fun getCustomFieldListQuery() = itemsBox.query().order(Item_.id).build()

    fun getCounter() = itemsBox.query().build().property(Item_.counter).sum()

    fun deleteItem(id: Long) {
        itemsBox.query().equal(Item_.id, id).build().remove()
    }

}