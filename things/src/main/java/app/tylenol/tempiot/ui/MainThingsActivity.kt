package app.tylenol.tempiot.ui

import android.os.Bundle
import app.tylenol.common.data.FirestoreEventController
import app.tylenol.common.ui.BaseActivity
import app.tylenol.tempiot.hardware.RelayController
import app.tylenol.tempiot.hardware.ThermostatController
import javax.inject.Inject

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
class MainThingsActivity : BaseActivity<MainThingsActivityLayout>() {

    @Inject lateinit var relayController: RelayController
    @Inject lateinit var thermostatController: ThermostatController

    override val layout = MainThingsActivityLayout()

    override fun MainThingsActivityLayout.setLayout() {
        setTemperature(36)
        setHumidity(5)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        relayController.start()
        thermostatController.start(this)
//        thermostatController.observeTempMode(this) {
//
//            var currentTemp = thermostatController.thermostatVM.tempLD.value ?: return@observeTempMode
//            var currentTempSetPoint = thermostatController.thermostatVM.tempSetPointLD.value ?: return@observeTempMode
//
//            FirestoreEventController.ThermostatMode.apply {
//                when (it) {
//                    ON -> relayController.setRelayValue(true)
//                    OFF -> relayController.setRelayValue(false)
////                    HEAT -> relayController.setRelayValue(currentTemp < currentTempSetPoint)
////                    COOL -> relayController.setRelayValue(currentTemp > currentTempSetPoint)
//                }
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        relayController.close()
        thermostatController.stop()
    }

}