package app.tylenol.tempiot._di

import android.app.Activity
import android.app.Application
import app.tylenol.tempiot._di.components.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasDispatchingActivityInjector
import javax.inject.Inject

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
class App : Application(), HasDispatchingActivityInjector {

    //TODO REMINDER: register this class in AndroidManifest.xml

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}