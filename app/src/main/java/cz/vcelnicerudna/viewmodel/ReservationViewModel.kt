package cz.vcelnicerudna.viewmodel

import android.util.Log
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.FieldPosition

class ReservationViewModel : ViewModel() {

    private val classTag = ReservationViewModel::class.simpleName
    private val glassesCount = listOf<Int>(1, 2, 3, 4, 5)
    private val glasses = ObservableField<Int>(glassesCount[0])
    val glassesCountEntries: ObservableField<List<Int>> = ObservableField(glassesCount)
    val email = ObservableField<String>("")
    val message = ObservableField<String>("")

    private val postLiveData = MutableLiveData<Boolean>()

    fun getPostLiveData(): LiveData<Boolean> = postLiveData

    fun updateGlassesCount(position: Int) {
        glasses.set(glassesCount[position])
    }

    fun postReservation() {
        Log.d(classTag, "Called: ${this.email}")
        if (canPostReservation()) {
            Log.d(classTag, "Posting reservation: email: ${this.email.get()}, message: ${this.message.get()}, glasses: ${this.glasses.get()}")
        }
    }

    private fun canPostReservation(): Boolean {
        return true
    }
}