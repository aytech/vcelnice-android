package cz.vcelnicerudna.contact

import cz.vcelnicerudna.data.model.EmailMessage

class ContactContract {
    interface ViewInterface {}
    interface PresenterInterface {
        fun postContactMessage(email: String, message: String)
    }
}