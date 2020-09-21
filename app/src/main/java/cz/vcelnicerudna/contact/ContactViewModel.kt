package cz.vcelnicerudna.contact

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import cz.vcelnicerudna.R
import cz.vcelnicerudna.configuration.Validator
import cz.vcelnicerudna.data.model.EmailMessage

class ContactViewModel : ViewModel() {
    val email: ObservableField<String> = ObservableField("")
    val message: ObservableField<String> = ObservableField("")
    var validationMessage: Int = 0

    fun canPostMessage(): Boolean {
        if (!Validator.isEmail(email = email.get())) {
            validationMessage = R.string.enter_valid_email
            return false
        }
        if (message.get().isNullOrEmpty()) {
            validationMessage = R.string.enter_message
            return false
        }
        return true
    }

    fun getEmailMessage(): EmailMessage {
        return EmailMessage(email.get(), message.get())
    }

    fun clearForm() {
        email.set(null)
        message.set(null)
    }
}