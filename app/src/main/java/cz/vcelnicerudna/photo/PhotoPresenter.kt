package cz.vcelnicerudna.photo

import android.util.Log
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Photo
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class PhotoPresenter(
        private var activity: PhotoContract.ViewInterface,
        private var vcelniceAPI: VcelniceAPI,
        private var localDataStore: AppDatabase) : PhotoContract.PresenterInterface {

    private val classTag = PhotoPresenter::class.simpleName
    private val compositeDisposable = CompositeDisposable()

    private val apiObservable : Observable<List<Photo>>
        get() = vcelniceAPI.getPhotos()
    private val apiObserver : DisposableObserver<List<Photo>>
        get() = object : DisposableObserver<List<Photo>>() {
            override fun onNext(photos: List<Photo>) {
                activity.showPhotos(photos)
                photos.forEach {
                    persistPhotos(it)
                }
                Log.d(classTag, "Got list of photos from API: $photos")
            }

            override fun onError(e: Throwable) {
                activity.onNetworkError()
                Log.d(classTag, "Error fetching photos")
            }

            override fun onComplete() {
                Log.d(classTag, "Fetching photos is now complete")
            }

        }

    private val localDataStoreObservable : Single<List<Photo>>
        get() = localDataStore.photoDao().getPhotos()
    private val localDataStoreObserver : DisposableSingleObserver<List<Photo>>
        get() = object : DisposableSingleObserver<List<Photo>>() {
            override fun onError(e: Throwable) {
                activity.showError()
                Log.d(classTag, "Error fetching list of photos from local DB")
            }

            override fun onSuccess(photos: List<Photo>) {
                activity.showPhotos(photos)
                Log.d(classTag, "Got list of photos from local DB: $photos")
            }

        }

    private val persistPhotoObserver: DisposableSingleObserver<Long>
        get() = object : DisposableSingleObserver<Long>() {
            override fun onSuccess(id: Long) {
                Log.d(classTag, "Persisted photo with ID $id")
            }

            override fun onError(e: Throwable) {
                Log.d(classTag, "Error persisting photo: $e")
            }

        }

    override fun fetchPhotosFromAPI() {
        val photoDisposable = apiObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(apiObserver)
        compositeDisposable.add(photoDisposable)
    }

    override fun fetchPhotosFromLocalDataStore() {
        val localStoreDisposable = localDataStoreObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(localDataStoreObserver)
        compositeDisposable.add(localStoreDisposable)
    }

    override fun persistPhotos(photo: Photo) {
        val disposable = localDataStore.photoDao().insert(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(persistPhotoObserver)
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}