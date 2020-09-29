package cz.vcelnicerudna.prices

import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Price
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PricesPresenter(
        private var activity: PricesContract.ViewInterface,
        private var vcelniceAPI: VcelniceAPI,
        private var localDataSource: AppDatabase) : PricesContract.PresenterInterface {

    private val compositeDisposable = CompositeDisposable()

    private val apiObservable: Observable<List<Price>>
        get() = vcelniceAPI.getPrices()
    private val apiObserver: DisposableObserver<List<Price>>
        get() = object : DisposableObserver<List<Price>>() {
            override fun onNext(prices: List<Price>) {
                activity.showPrices(prices)
                prices.forEach { persistPrice(it) }
            }

            override fun onError(e: Throwable) {
                activity.onNetworkError()
            }

            override fun onComplete() {
                activity.onPricesLoaded()
            }
        }

    private val localDataStoreObservable: Single<List<Price>>
        get() = localDataSource.pricesDao().getPrices()
    private val localDataStoreObserver: DisposableSingleObserver<List<Price>>
        get() = object : DisposableSingleObserver<List<Price>>() {
            override fun onSuccess(prices: List<Price>) {
                activity.showPrices(prices)
            }

            override fun onError(e: Throwable) {
                activity.showError()
            }
        }

    private val persistPriceObserver: DisposableSingleObserver<Long>
        get() = object : DisposableSingleObserver<Long>() {
            override fun onSuccess(id: Long) {
                Timber.d("Persisted price with ID $id")
            }

            override fun onError(error: Throwable) {
                Timber.d("Error while trying to persist price, $error")
            }
        }

    override fun fetchPricesFromApi() {
        val pricesDisposable = apiObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(apiObserver)
        compositeDisposable.add(pricesDisposable)
    }

    override fun fetchPricesFromLocalDataStore() {
        val localStoreDisposable = localDataStoreObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(localDataStoreObserver)
        compositeDisposable.add(localStoreDisposable)
    }

    override fun persistPrice(price: Price) {
        val disposable = localDataSource.pricesDao().insert(price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(persistPriceObserver)
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}