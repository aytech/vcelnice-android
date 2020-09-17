package cz.vcelnicerudna.reserve

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.Validator
import cz.vcelnicerudna.models.Location

class ReserveViewModel : ViewModel() {
    private val glassesData: List<Int> = listOf(1, 2, 3, 4, 5)
    lateinit var reservationTitle: String
    var validationMessage: Int = 0
    val glassesCount = ObservableField(glassesData[0])
    val locationEntry = ObservableField<Location>()
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
}