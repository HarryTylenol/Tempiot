package app.tylenol.tempiot.hardware

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.hardware.usb.UsbDevice
import app.tylenol.tempiot.util.tryCatch
import me.aflak.arduino.Arduino
import me.aflak.arduino.ArduinoListener
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.runOnUiThread


/**
 * Created by baghyeongi on 2018. 4. 12..
 */
class ThermostatViewModel : ViewModel(), AnkoLogger {

    private var arduino: Arduino? = null

    val tempLD: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val tempSetPointLD: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val tempModeLD: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun fetchTemp(context: Context) = with(context) {
        arduino = Arduino(context)

        arduino?.setArduinoListener(object : ArduinoListener {
            override fun onArduinoAttached(device: UsbDevice?) {
                arduino?.open(device)
            }

            override fun onArduinoMessage(bytes: ByteArray?) {
                bytes?.let {
                    tryCatch {
                        val data = String(it)
                        if (data.contains(".00") && data != ".00") {
                            runOnUiThread {
                                tempLD.value = data.trim().toFloat().toLong()
                            }
                        }
                    }
                }
            }

            override fun onArduinoDetached() {

            }

            override fun onArduinoOpened() {
            }

            override fun onUsbPermissionDenied() {
            }
        })

    }

    fun stopFetching() {
        arduino?.close()
    }

}