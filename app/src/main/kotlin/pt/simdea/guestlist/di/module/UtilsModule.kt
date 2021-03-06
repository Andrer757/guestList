package pt.simdea.guestlist.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import pt.simdea.guestlist.utils.helper.*
import pt.simdea.guestlist.utils.manager.*
import javax.inject.Singleton

@Module
class UtilsModule {

    @Singleton
    @Provides
    fun providesIntentManager() = CallManager()

    @Singleton
    @Provides
    fun providesDialogManager() = DialogManager()

    @Singleton
    @Provides
    fun providesPreferencesManager(application: Application) = PreferencesManager(application)

    @Singleton
    @Provides
    fun providesPermissionHelper() = PermissionHelper()

    @Singleton
    @Provides
    fun providesGPSHelper(application: Application, permissionHelper: PermissionHelper) = GPSHelper(application, permissionHelper)

}