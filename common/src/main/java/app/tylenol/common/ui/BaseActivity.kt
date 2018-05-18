package app.tylenol.common.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.support.DaggerAppCompatActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.setContentView

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
abstract class BaseActivity<Layout : AnkoComponent<AppCompatActivity>> : DaggerAppCompatActivity() {

    abstract val layout: Layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout.setContentView(this)
        layout.setLayout()
    }

    abstract fun Layout.setLayout()

}