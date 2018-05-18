package app.tylenol.tempiot._di.factories

import android.app.Activity
import app.tylenol.tempiot.ui.MainThingsActivity
import app.tylenol.tempiot.ui.MainThingsActivitySubComponent
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
@Module
abstract class ActivitiesInjectorFactories {
    @Binds
    @IntoMap
    @ActivityKey(MainThingsActivity::class)
    internal abstract fun bindMainThingsActivityInjectorFactory(
            builder: MainThingsActivitySubComponent.Builder): AndroidInjector.Factory<out Activity>
}