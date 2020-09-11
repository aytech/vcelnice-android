package cz.vcelnicerudna.reserve

import android.util.Log
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.data.PricesRepository
import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.Location
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservePresenter(
        private var activity: ReserveContract.ViewInterface,
        private var pricesRepository: PricesRepository,
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
                activity.loadingComplete()
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
                Log.d(ReservePresenter::class.simpleName, "Persisted location with ID $id")
            }

            override fun onError(e: Throwable) {
                Log.d(ReservePresenter::class.simpleName, "Error persisting location: $e")
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
        pricesRepository
                .postReservation(reservation)
                .enqueue(object : Callback<Response<Void>> {
                    override fun onResponse(call: Call<Response<Void>>, response: Response<Response<Void>>) {
                        activity.onSuccessPostReservation()
                    }

                    override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                        activity.onFailPostReservation()
                    }
                })
    }
}