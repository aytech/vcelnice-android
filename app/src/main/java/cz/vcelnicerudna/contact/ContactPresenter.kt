package cz.vcelnicerudna.contact

import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.data.model.EmailMessage

class ContactPresenter(
        private var activity: ContactContract.ViewInterface,
        private var repository: Repository) : ContactContract.PresenterInterface {

    override fun postContactMessage(message: EmailMessage) {}

}