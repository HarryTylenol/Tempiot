package app.tylenol.common.ui

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import thingsApp.tylenol.common.R

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
object UIDefaults {

    fun productSans(context : Context) = ResourcesCompat.getFont(context, R.font.product_sans_regular)
}