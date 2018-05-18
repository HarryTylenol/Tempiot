package app.tylenol.tempiot.ui

import dagger.Subcomponent
import dagger.android.AndroidInjector

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
@Subcomponent
interface MainThingsActivitySubComponent : AndroidInjector<MainThingsActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainThingsActivity>()

}