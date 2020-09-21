package cz.vcelnicerudna.contact

import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.data.model.EmailMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class ContactPresenter(
        private var activity: ContactContract.ViewInterface,
        private var repository: Repository) : ContactContract.PresenterInterface {

    private val compositeDisposable = CompositeDisposable()

    private val postMessageObserver: DisposableObserver<EmailMessage>
        get() = object : DisposableObserver<EmailMessage>() {
            override fun onNext(response: EmailMessage) {
                activity.onMessageSent()
            }

            override fun onError(error: Throwable) {
                activity.onMessageSentError()
            }

            override fun onComplete() {
                activity.onMessageSendComplete()
            }
        }

    override fun postContactMessage(emailMessage: EmailMessage) {
        val disposable = repository.postContactMessage(message = emailMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(postMessageObserver)
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}