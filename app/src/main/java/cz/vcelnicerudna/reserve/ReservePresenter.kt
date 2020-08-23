package cz.vcelnicerudna.reserve

import android.util.Log
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Location
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

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
                TODO("Not yet implemented")
            }

            override fun onComplete() {
                Log.d(classTag, "Loading of locations is complete")
            }

        }

    override fun fetchLocationsFromAPI() {
        TODO("Not yet implemented")
    }

}