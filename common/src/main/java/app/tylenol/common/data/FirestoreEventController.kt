package app.tylenol.common.data

import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.AnkoLogger
import java.lang.Exception
import javax.inject.Inject

/**
 * Created by baghyeongi on 2018. 4. 11..
 */
open class FirestoreEventController @Inject constructor(var firestore: FirebaseFirestore) : AnkoLogger {

    class ThermostatData {
        var temp : Long = 0L
        var setpoint : Long = 0L
        var mode : String = ""
    }

    companion object {
        const val DEVICES = "devices"
        const val THERMOSTAT = "thermostat"

        const val TEMP = "temp"

        const val SET_POINT = "setpoint"
        const val MODE = "mode"
        const val VALUE = "value"

    }

    object ThermostatMode {
        const val ON = "on"
        const val OFF = "off"
        const val COOL = "cool"
        const val HEAT = "heat"
    }

    private val mainRef get() = firestore.collection(DEVICES)

    // Updates
    fun updateThermostatMode(mode: String) {
        mainRef.document(THERMOSTAT).update(mapOf(MODE to mode)).addOnFailureListener(Exception::printStackTrace)
    }

    fun updateTempValue(value: Long) {
        mainRef.document(THERMOSTAT).update(mapOf(TEMP to value)).addOnFailureListener(Exception::printStackTrace)
    }

    fun updateSetTempValue(value: Long) {
        mainRef.document(THERMOSTAT).update(mapOf(SET_POINT to value)).addOnFailureListener(Exception::printStackTrace)
    }

    fun observeDataChange(callback: (ThermostatData) -> Unit) {
        mainRef.document(THERMOSTAT).addSnapshotListener { docSnapshot, _ ->
            callback(docSnapshot.toObject(ThermostatData::class.java))
        }
    }

}