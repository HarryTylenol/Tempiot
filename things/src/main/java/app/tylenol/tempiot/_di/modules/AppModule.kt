package app.tylenol.tempiot._di.modules

import android.content.Context
import app.tylenol.tempiot._di.ThingsApp
import app.tylenol.tempiot.ui.MainThingsActivitySubComponent
import com.google.android.things.pio.PeripheralManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
@Module(subcomponents = [MainThingsActivitySubComponent::class])
class AppModule {

    @Singleton
    @Provides
    internal fun provideContext(application: ThingsApp): Context = application.applicationContext

    @Singleton
    @Provides
    internal fun providePeripheralManager() = PeripheralManager.getInstance()

}