package pt.simdea.guestlist.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): BoxStore = MyObjectBox.builder().androidContext(application).build()

}