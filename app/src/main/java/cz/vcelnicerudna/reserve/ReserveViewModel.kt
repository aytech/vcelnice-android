package cz.vcelnicerudna.reserve

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.StringConstants
import cz.vcelnicerudna.configuration.Validator
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.data.model.Location
import java.net.URLEncoder

class ReserveViewModel : ViewModel() {
    private val glassesData: List<Int> = listOf(1, 2, 3, 4, 5)
    lateinit var reservationTitle: String
    var icon: String? = null
    var validationMessage: Int = 0
    private val glassesCount = ObservableField(glassesData[0])
    private val locationEntry = ObservableField<Location>()
    val glassesCountEntries: ObservableField<List<Int>> = ObservableField(glassesData)
    val locationEntries: ObservableField<List<Location>> = ObservableField(listOf())
    val email = ObservableField("")
    val message = ObservableField("")

    fun updateGlassesCount(position: Int) {
        glassesCount.set(glassesData[position])
    }

    fun updateLocations(locations: List<Location>?) {
        locationEntries.set(locations)
    }

    fun updateLocation(position: Int) {
        locationEntry.set(locationEntries.get()?.get(position))
    }

    fun canPostReservation(): Boolean {
        return if (!Validator.isEmail(email.get())) {
            validationMessage = R.string.enter_valid_email
            false
        } else {
            true
        }
    }

    fun getReservation(): Reservation {
        return Reservation(
                reservationTitle,
                glassesCount.get(),
                locationEntry.get().toString(),
                email.get(),
                URLEncoder.encode(message.get(), StringConstants.UTF_8))
    }
}