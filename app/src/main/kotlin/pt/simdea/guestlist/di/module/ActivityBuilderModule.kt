package pt.simdea.guestlist.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pt.simdea.guestlist.ui.splash.SplashActivity

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributesSplashActivity(): SplashActivity

}