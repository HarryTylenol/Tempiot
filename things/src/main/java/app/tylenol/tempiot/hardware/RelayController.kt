package app.tylenol.tempiot.hardware

import app.tylenol.common.data.FirestoreEventController
import app.tylenol.tempiot.constant.BoardDefaults
import app.tylenol.tempiot.util.tryCatch
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.GpioCallback
import com.google.android.things.pio.PeripheralManager
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.onComplete
import javax.inject.Inject

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
class RelayController @Inject constructor(
        private var fec: FirestoreEventController,
        private var peripheralManager: PeripheralManager
) : AnkoLogger {

    private var relay: Gpio? = null

    private val relayValueCallback = object : GpioCallback {
        override fun onGpioEdge(gpio: Gpio?): Boolean {
            info("GPIO Changed : ${gpio?.value}")
            return true
        }
        override fun onGpioError(gpio: Gpio?, error: Int) {
            info("GPIO Error : ${error}")
        }
    }

    fun start() {
        tryCatch {
            doAsync {
                relay = peripheralManager.openGpio(BoardDefaults.RELAY_GPIO_NAME).apply {
                    setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
                    setActiveType(Gpio.ACTIVE_HIGH)
                }
                onComplete {
                    info("Relay Connection : ${relay != null}")
                    relay?.registerGpioCallback(relayValueCallback)
                    fec.observeDataChange {
                        when (it.mode) {
                            FirestoreEventController.ThermostatMode.ON -> setRelayValue(true)
                            FirestoreEventController.ThermostatMode.OFF -> setRelayValue(false)
                            FirestoreEventController.ThermostatMode.COOL -> setRelayValue(it.temp > it.setpoint)
                            FirestoreEventController.ThermostatMode.HEAT -> setRelayValue(it.temp < it.setpoint)
                        }
                    }
                }
            }
        }
    }

    fun close() {
        tryCatch {
            relay?.unregisterGpioCallback(relayValueCallback)
            relay?.close()
        }
    }

    fun setRelayValue(isOn: Boolean) {
        relay?.value = isOn
    }

}