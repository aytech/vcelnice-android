package cz.vcelnicerudna.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import cz.vcelnicerudna.models.Location

class ReservationViewModel : ViewModel() {

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
            Log.d(ReservationViewModel::class.simpleName, "Posting reservation: email: ${this.email.get()}, message: ${this.message.get()}, glasses: ${this.glassesCount.get()}, location: ${locationEntry.get()}")
        } else {
            emailErrorVisible.set(true)
        }
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