package app.tylenol.tempiot._di.components

import app.tylenol.common.module.FirebaseModule
import app.tylenol.tempiot._di.ThingsApp
import app.tylenol.tempiot._di.factories.ActivitiesInjectorFactories
import app.tylenol.tempiot._di.factories.FragmentsInjectorFactories
import app.tylenol.tempiot._di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        ActivitiesInjectorFactories::class,
        FragmentsInjectorFactories::class,
        AppModule::class,
        FirebaseModule::class
))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: ThingsApp): Builder

        fun build(): AppComponent
    }

    fun inject(thingsApp: ThingsApp)

}