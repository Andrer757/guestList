package pt.simdea.guestlist

import android.app.Application

import io.objectbox.BoxStore

class App : Application() {
    var boxStore: BoxStore? = null
        private set

    override fun onCreate() {
        super.onCreate()

        boxStore = MyObjectBox.builder().androidContext(this@App).build()
    }
}
