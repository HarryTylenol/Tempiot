package app.tylenol.tempiot.ui

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import app.tylenol.common.ui.UIDefaults
import org.jetbrains.anko.*

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
class MainThingsActivityLayout : AnkoComponent<AppCompatActivity> {

    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView

    fun setTemperature(temp: Int) {
        temperatureTextView.text = "$temp°C"
    }

    fun setHumidity(hum: Int) {
        humidityTextView.text = "$hum%"
    }

    override fun createView(ui: AnkoContext<AppCompatActivity>) = with(ui) {
        frameLayout {
            backgroundColor = Color.BLACK
            padding = dip(36)
            verticalLayout {
                temperatureTextView = textView {
                    textSize = 72f
                    typeface = UIDefaults.productSans(context)
                }.lparams(matchParent, dip(0)) {
                    weight = 7f
                }
                humidityTextView = textView {
                    textSize = 32f
                    typeface = UIDefaults.productSans(context)
                    alpha = 0.3f
                }.lparams(matchParent, dip(0)) {
                    weight = 3f
                }

            }.lparams(matchParent).applyRecursively {
                when (it) {
                    is TextView -> it.textColor = Color.WHITE
                }
            }
        }
    }
}