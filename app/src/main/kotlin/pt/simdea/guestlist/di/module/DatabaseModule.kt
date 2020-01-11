package pt.simdea.guestlist.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import pt.simdea.guestlist.db.DatabaseProvider
import pt.simdea.guestlist.db.model.MyObjectBox
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): BoxStore = MyObjectBox.builder().androidContext(application).build()

    @Singleton
    @Provides
    fun providesDatabaseProvider(boxStore: BoxStore) = DatabaseProvider(boxStore)

}