package cz.vcelnicerudna.main

import android.util.Log
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.HomeText
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainPresenter(
        private var viewInterface: MainContract.ViewInterface,
        private var vcelniceAPI: VcelniceAPI,
        private var localDataStore: AppDatabase) : MainContract.PresenterInterface {

    private val classTag = MainPresenter::class.java.simpleName
    private val compositeDisposable = CompositeDisposable()

    private val apiObservable: Observable<HomeText>
        get() = vcelniceAPI.getHomeText()
    private val apiObserver: DisposableObserver<HomeText>
        get() = object : DisposableObserver<HomeText>() {
            override fun onNext(text: HomeText) {
                viewInterface.showHomeText(text)
                persistHomeText(text)
            }

            override fun onError(e: Throwable) {
                viewInterface.onNetworkError()
            }

            override fun onComplete() {
                Log.d(classTag, "Loading text complete")
            }
        }

    private val localDataStoreSingleObservable: Single<HomeText>
        get() = localDataStore.homeDao().getHomeText()
    private val localDataStoreSingleObserver: DisposableSingleObserver<HomeText>
        get() = object : DisposableSingleObserver<HomeText>() {
            override fun onSuccess(text: HomeText) {
                viewInterface.showHomeText(text)
            }

            override fun onError(e: Throwable) {
                viewInterface.showError()
            }
        }

    override fun fetchHomeTextFromApi() {
        val homeTextDisposable = apiObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(apiObserver)
        compositeDisposable.add(homeTextDisposable)
    }

    override fun fetchHomeTextFromLocalDataStore() {
        val localStoreDisposable = localDataStoreSingleObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(localDataStoreSingleObserver)
        compositeDisposable.add(localStoreDisposable)
    }

    fun persistHomeText(text: HomeText) {
        val disposable = localDataStore.homeDao().insert(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Long>() {
                    override fun onSuccess(id: Long) {
                        Log.d(classTag, "Persisted with ID $id")
                    }

                    override fun onError(e: Throwable) {
                        Log.d(classTag, "Error persisting data: $e")
                    }
                })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}