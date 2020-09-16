package cz.vcelnicerudna.contact

import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.data.model.EmailMessage
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Response

class ContactPresenter(
        private var activity: ContactContract.ViewInterface,
        private var repository: Repository) : ContactContract.PresenterInterface {

    override fun postContactMessage(message: EmailMessage) {
        repository
    }

}