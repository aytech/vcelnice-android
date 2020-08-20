package cz.vcelnicerudna.main

import android.util.Log
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.HomeText
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MainPresenter(private var viewInterface: MainContract.ViewInterface): MainContract.PresenterInterface {

    private val vcelniceAPI by lazy {
        VcelniceAPI.create()
    }

    private val classTag = "MainPresenter"
    private val compositeDisposable = CompositeDisposable()
    private val homeTextObservable: Observable<HomeText>
        get() = vcelniceAPI.getHomeText()
    private val observer: DisposableObserver<HomeText>
        get() = object : DisposableObserver<HomeText>() {
            override fun onNext(t: HomeText) {
                Log.d(classTag, "OnNext: $t")
            }

            override fun onError(e: Throwable) {
                Log.d(classTag, "OnError: $e")
            }

            override fun onComplete() {
                Log.d(classTag, "Loading complete")
            }

        }

    override fun fetchHomeTextFromApi() {
        val homeTextDisposable = homeTextObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
        compositeDisposable.add(homeTextDisposable)
    }
}