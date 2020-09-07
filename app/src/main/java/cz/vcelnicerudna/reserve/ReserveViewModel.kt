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
    private val glassesCount = ObservableField(glassesData[0])
    private val locationEntry = ObservableField<Location>()
    val glassesCountEntries: ObservableField<List<Int>> = ObservableField(glassesData)
    val locationEntries: ObservableField<List<Location>> = ObservableField(listOf())
    val email = ObservableField("")
    val message = ObservableField("")
    val emailErrorVisible = ObservableField(false)

    fun updateGlassesCount(position: Int) {
        glassesCount.set(glassesData[position])
    }

    fun updateLocations(locations: List<Location>) {
        locationEntries.set(locations)
    }

    fun updateLocation(position: Int) {
        locationEntry.set(locationEntries.get()?.get(position))
    }

    fun postReservation() {
        emailErrorVisible.set(false)
        if (canPostReservation()) {
            val reservation = Reservation(
                    title = "foo",
                    amount = glassesCount.get(),
                    location = locationEntry.get().toString(),
                    email = email.get(),
                    message = message.get()
            )
            repository.postReservation(reservation)
            resetForm()
        } else {
            emailErrorVisible.set(true)
        }
    }

    fun resetForm() {
        updateGlassesCount(0)
        updateLocation(0)
        email.set("")
        message.set("")
    }

    private fun canPostReservation(): Boolean {
        val emailAddress = email.get()
        return if (emailAddress.isNullOrEmpty()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(emailAddress as CharSequence).matches()
        }
    }
}