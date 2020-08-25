package cz.vcelnicerudna.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReservationViewModel : ViewModel() {

    private val classTag = ReservationViewModel::class.simpleName
    var email = ObservableField<String>("")
    var message = ObservableField<String>("")

    private val postLiveData = MutableLiveData<Boolean>()

    fun getPostLiveData(): LiveData<Boolean> = postLiveData

    fun postReservation() {
        if (canPostReservation()) {
            Log.d(classTag, "Posting reservation: email: ${this.email.get()}, message: ${this.message.get()}")
        }
    }

    private fun canPostReservation(): Boolean {
        return false
    }
}