package cz.vcelnicerudna.main

import android.util.Log
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.AppDatabaseWorkerThread
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
        private var localDataStore: AppDatabase,
        private var appDatabaseWorkerThread: AppDatabaseWorkerThread) : MainContract.PresenterInterface {

    private val classTag = MainPresenter::class.java.simpleName
    private val compositeDisposable = CompositeDisposable()

    private val apiObservable: Observable<HomeText>
        get() = vcelniceAPI.getHomeText()
    private val apiObserver: DisposableObserver<HomeText>
        get() = object : DisposableObserver<HomeText>() {
            override fun onNext(text: HomeText) {
                viewInterface.showHomeText(text)
                val task = Runnable { localDataStore.homeDao().insert(text) }
                appDatabaseWorkerThread.postTask(task)
            }

            override fun onError(e: Throwable) {
                viewInterface.onNetworkError()
            }

            override fun onComplete() {
                Log.d(classTag, "Loading complete")
            }
        }

    private val localDataStoreSingleObservable: Single<HomeText>
        get() = localDataStore.homeDao().getHomeText()
    private val localDataStoreSingleObserver: DisposableSingleObserver<HomeText>
        get() = object : DisposableSingleObserver<HomeText>() {
            override fun onSuccess(text: HomeText) {
                Log.d(classTag, "onSuccess: $text")
                viewInterface.showHomeText(text)
            }

            override fun onError(e: Throwable) {
                Log.d(classTag, "onError: $e")
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


    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}