package pt.simdea.guestlist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pt.simdea.guestlist.di.annotations.ViewModelKey
import pt.simdea.guestlist.di.factory.ViewModelFactory
import pt.simdea.guestlist.ui.main.MainViewModel
import pt.simdea.guestlist.ui.splash.SplashViewModel

@Module(includes = [RepositoryModule::class])
abstract class ViewModelModule {

    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindsSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindsMainViewModel(mainViewModel: MainViewModel): ViewModel

}