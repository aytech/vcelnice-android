package cz.vcelnicerudna.reserve

import android.util.Log
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Location
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ReservePresenter(
        private var activity: ReserveContract.ViewInterface,
        private var vcelniceAPI: VcelniceAPI,
        private var localDataStore: AppDatabase) : ReserveContract.PresenterInterface {

    private val classTag = ReservePresenter::class.simpleName
    private val compositeDisposable = CompositeDisposable()

    private val apiObservable: Observable<List<Location>>
        get() = vcelniceAPI.getLocations()
    private val apiObserver: DisposableObserver<List<Location>>
        get() = object : DisposableObserver<List<Location>>() {
            override fun onNext(locations: List<Location>) {
                activity.showLocations(locations)
            }

            override fun onError(e: Throwable) {
                activity.onNetworkError()
            }

            override fun onComplete() {
                Log.d(classTag, "Loading of locations is complete")
            }
        }

    private val localDataStoreObservable: Single<List<Location>>
        get() = localDataStore.locationsDao().getLocations()
    private val localDataStoreObserver: DisposableSingleObserver<List<Location>>
        get() = object : DisposableSingleObserver<List<Location>>() {
            override fun onSuccess(locations: List<Location>) {
                activity.showLocations(locations)
            }

            override fun onError(e: Throwable) {
                Log.d(classTag, "Error fetching locations from local store: $e")
            }
        }

    private val persistLocationObserver: DisposableSingleObserver<Long>
        get() = object : DisposableSingleObserver<Long>() {
            override fun onSuccess(id: Long) {
                Log.d(classTag, "Persisted location with ID $id")
            }

            override fun onError(e: Throwable) {
                Log.d(classTag, "Error persisting location: $e")
            }
        }

    override fun fetchLocationsFromAPI() {
        val disposable = apiObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(apiObserver)
        compositeDisposable.add(disposable)
    }

    override fun fetchLocationsFromLocalDataStore() {
        val disposable = localDataStoreObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(localDataStoreObserver)
        compositeDisposable.add(disposable)
    }

    override fun persistLocation(location: Location) {
        val disposable = localDataStore.locationsDao().insert(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(persistLocationObserver)
        compositeDisposable.add(disposable)
    }
}