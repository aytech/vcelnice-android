package cz.vcelnicerudna.reserve

import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import cz.vcelnicerudna.data.PricesRepository
import cz.vcelnicerudna.data.PricesRepositoryImpl
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.Location

class ReserveViewModel(private val repository: PricesRepository = PricesRepositoryImpl()) : ViewModel() {
    private val glassesData = listOf(1, 2, 3, 4, 5)
    val glassesCount = ObservableField(glassesData[0])
    val locationEntry = ObservableField<Location>()
    val glassesCountEntries: ObservableField<List<Int>> = ObservableField(glassesData)
    val locationEntries: ObservableField<List<Location>> = ObservableField(listOf())
    val email = ObservableField("")
    val message = ObservableField("")
    val emailErrorVisible = ObservableField(false)

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
        val emailAddress = email.get()
        return if (emailAddress.isNullOrEmpty()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(emailAddress as CharSequence).matches()
        }
    }
}