package cz.vcelnicerudna.contact

import android.util.Log
import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.data.model.EmailMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*

class ContactPresenter(
        private var activity: ContactContract.ViewInterface,
        private var repository: Repository) : ContactContract.PresenterInterface {

    private val compositeDisposable = CompositeDisposable()

    override fun postContactMessage(email: String, message: String) {
        val emailMessage = EmailMessage(email, message)
        val disposable = repository.postContactMessage(message = emailMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Response<Void>>() {
                    override fun onNext(t: Response<Void>) {
                        Log.d(ContactPresenter::class.simpleName, "onNext: $t")
                    }

                    override fun onError(e: Throwable) {
                        Log.d(ContactPresenter::class.simpleName, "onError: $e")
                    }

                    override fun onComplete() {
                        Log.d(ContactPresenter::class.simpleName, "onComplete")
                    }
                })
        compositeDisposable.add(disposable)
    }

}