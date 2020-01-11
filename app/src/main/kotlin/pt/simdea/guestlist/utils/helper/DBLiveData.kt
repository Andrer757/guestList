package pt.simdea.guestlist.utils.helper

import androidx.lifecycle.LiveData
import io.objectbox.query.Query
import io.objectbox.reactive.DataObserver
import io.objectbox.reactive.DataSubscription

class DBLiveData<T>(private val query: Query<T>) : LiveData<List<T>>() {

    private var subscription: DataSubscription? = null
    private val listener: DataObserver<List<T>> = DataObserver { data -> postValue(data) }

    override fun onActive() {
        super.onActive()

        if (subscription == null) {
            subscription = query.subscribe().observer(listener)
        }
    }

    override fun onInactive() {
        super.onInactive()

        if (hasObservers()) {
            subscription?.cancel()
            subscription = null
        }
    }

}