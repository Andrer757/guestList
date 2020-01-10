package pt.simdea.guestlist.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.AndroidInjectionModule
import pt.simdea.guestlist.app.App
import pt.simdea.guestlist.di.module.*
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityBuilderModule::class,
    DatabaseModule::class,
    DialogBuilderModule::class,
    FragmentBuilderModule::class,
    UtilsModule::class,
    ApiModule::class,
    ReceiverModule::class,
    ServiceModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }

    override fun inject(app: App)

}