package cz.vcelnicerudna.contact

import cz.vcelnicerudna.data.model.EmailMessage

class ContactContract {
    interface ViewInterface {
        fun onMessageSent()
        fun onMessageSentError()
        fun onMessageSendComplete()
    }

    interface PresenterInterface {
        fun postContactMessage(emailMessage: EmailMessage)
        fun onDestroy()
    }
}