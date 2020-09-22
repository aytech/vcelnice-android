package cz.vcelnicerudna.reserve

import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.Location
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ReservePresenter(
        private var activity: ReserveContract.ViewInterface,
        private var pricesRepository: Repository,
        private var localDataStore: AppDatabase) : ReserveContract.PresenterInterface {

    private val compositeDisposable = CompositeDisposable()

    private val locationsRepositoryObservable: Observable<List<Location>>
        get() = pricesRepository.getReservationLocations()
    private val locationsRepositoryObserver: DisposableObserver<List<Location>>
        get() = object : DisposableObserver<List<Location>>() {
            override fun onNext(locations: List<Location>) {
                activity.showLocations(locations)
            }

            override fun onError(e: Throwable) {
                activity.onNetworkError()
            }

            override fun onComplete() {
                activity.onLocationsFetchComplete()
            }

        }

    private val localDataStoreObservable: Single<List<Location>>
        get() = localDataStore.locationsDao().getLocations()
    private val localDataStoreObserver: DisposableSingleObserver<List<Location>>
        get() = object : DisposableSingleObserver<List<Location>>() {
            override fun onSuccess(locations: List<Location>) {
                activity.showLocations(locations)
            }

            override fun onError(error: Throwable) {
                activity.showDefaultLocation()
            }
        }

    private val persistLocationObserver: DisposableSingleObserver<Long>
        get() = object : DisposableSingleObserver<Long>() {
            override fun onSuccess(id: Long) {
                Timber.d("Persisted location with ID $id")
            }

            override fun onError(error: Throwable) {
                Timber.d("Error persisting location: $error")
            }
        }

    private val postReservationObserver: DisposableObserver<Reservation>
        get() = object : DisposableObserver<Reservation>() {
            override fun onNext(reservation: Reservation) {
                activity.onSuccessPostReservation()
            }

            override fun onError(error: Throwable) {
                Timber.d("Error posting reservation to API: $error")
                activity.onFailPostReservation()
            }

            override fun onComplete() {
                activity.onCompletePostReservation()
            }
        }

    override fun fetchLocationsFromApi() {
        val locationsDisposable = locationsRepositoryObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(locationsRepositoryObserver)
        compositeDisposable.add(locationsDisposable)
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

    override fun postReservation(reservation: Reservation) {
        val disposable = pricesRepository
                .postReservation(reservation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(postReservationObserver)
        compositeDisposable.add(disposable)
    }
}