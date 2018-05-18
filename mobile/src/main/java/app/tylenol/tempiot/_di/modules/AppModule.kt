package app.tylenol.tempiot._di.modules

import android.content.Context
import app.tylenol.tempiot._di.App
import app.tylenol.tempiot.ui.MainActivitySubComponent
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
@Module(subcomponents = [MainActivitySubComponent::class])
class AppModule {

    @Singleton
    @Provides
    internal fun provideContext(application: App): Context = application.applicationContext
}