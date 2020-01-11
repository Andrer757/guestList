package pt.simdea.guestlist.ui.main

import androidx.lifecycle.ViewModel
import pt.simdea.guestlist.db.DatabaseProvider
import pt.simdea.guestlist.db.model.Item
import pt.simdea.guestlist.utils.helper.DBLiveData
import javax.inject.Inject

class MainViewModel @Inject constructor(private val databaseProvider: DatabaseProvider) : ViewModel() {

    internal var itemsLiveData: DBLiveData<Item>? = null
        get() {
            if (field == null) {
                // query all notes, sorted a-z by their text
                field = DBLiveData(databaseProvider.getCustomFieldListQuery())
            }
            return field
        }

    fun addItem(name: String) {
        databaseProvider.saveItem(name)
    }

    fun deleteItem(id: Long) {
        databaseProvider.deleteItem(id)
    }

    fun updateItem(id: Long) {
        databaseProvider.updateCount(id)
    }

    fun getCounter() = databaseProvider.getCounter()

}
