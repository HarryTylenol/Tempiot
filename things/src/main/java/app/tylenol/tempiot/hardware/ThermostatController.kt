package app.tylenol.tempiot.hardware

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v4.app.FragmentActivity
import app.tylenol.common.data.FirestoreEventController
import app.tylenol.tempiot.constant.BoardDefaults
import app.tylenol.tempiot.util.tryCatch
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
class ThermostatController @Inject constructor(var context: Context,
                                               var peripheralManager: PeripheralManager,
                                               var fec: FirestoreEventController) : LifecycleObserver, AnkoLogger {

    private var thermostat: Gpio? = null
    var thermostatVM: ThermostatViewModel by Delegates.notNull()

    fun observeTempMode(owner: LifecycleOwner, callback: (String) -> Unit) = thermostatVM.tempModeLD.observe(owner, Observer<String> { temp ->
        temp?.let {
            callback(it)
        }
    })

    fun observeTempSetPoint(owner: LifecycleOwner, callback: (Long) -> Unit) = thermostatVM.tempSetPointLD.observe(owner, Observer<Long> { temp ->
        temp?.let {
            callback(it)
        }
    })

    fun observeTemp(owner: LifecycleOwner, callback: (Long) -> Unit) = thermostatVM.tempLD.observe(owner, Observer<Long> { temp ->
        temp?.let {
            //            fec.updateTempValue(it)
            callback(it)
        }
    })

    var lastUpdated: Long? = null
    val updateOffset = 5000L

    fun start(fragmentActivity: FragmentActivity) {

        tryCatch {
            thermostat = peripheralManager.openGpio(BoardDefaults.DHT11_GPIO_NAME)

            thermostat?.setActiveType(Gpio.ACTIVE_LOW)
            thermostat?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH)

            thermostatVM = ViewModelProviders.of(fragmentActivity).get(ThermostatViewModel::class.java)

            thermostatVM.fetchTemp(fragmentActivity)
            thermostatVM.tempLD.observe(fragmentActivity, Observer {
                if (lastUpdated == null) {
                    lastUpdated = System.currentTimeMillis()
                    if (it != null) fec.updateTempValue(it)
                }
                else if (System.currentTimeMillis() - lastUpdated!! > updateOffset) {
                    if (it != null) {
                        fec.updateTempValue(it)
                        lastUpdated = System.currentTimeMillis()
                    }
                }
            })

        }
    }

    fun stop() {
        tryCatch {
            thermostat?.close()
        }
    }

}