package cz.vcelnicerudna.photo

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
import timber.log.Timber

class PhotoPresenter(
        private var activity: PhotoContract.ViewInterface,
        private var vcelniceAPI: VcelniceAPI,
        private var localDataStore: AppDatabase) : PhotoContract.PresenterInterface {

    private val compositeDisposable = CompositeDisposable()

    private val apiObservable: Observable<List<Photo>>
        get() = vcelniceAPI.getPhotos()
    private val apiObserver: DisposableObserver<List<Photo>>
        get() = object : DisposableObserver<List<Photo>>() {
            override fun onNext(photos: List<Photo>) {
                activity.showPhotos(photos)
                photos.forEach {
                    persistPhotos(it)
                }
            }

            override fun onError(error: Throwable) {
                Timber.d("Error fetching photos from API: $error")
                activity.onNetworkError()
            }

            override fun onComplete() {
                Timber.d("Finished loading photos")
            }

        }

    private val localDataStoreObservable: Single<List<Photo>>
        get() = localDataStore.photosDao().getPhotos()
    private val localDataStoreObserver: DisposableSingleObserver<List<Photo>>
        get() = object : DisposableSingleObserver<List<Photo>>() {
            override fun onError(error: Throwable) {
                Timber.d("Error fetching photos from local DB: $error")
                activity.showError()
            }

            override fun onSuccess(photos: List<Photo>) {
                activity.showPhotos(photos)
            }

        }

    private val persistPhotoObserver: DisposableSingleObserver<Long>
        get() = object : DisposableSingleObserver<Long>() {
            override fun onSuccess(id: Long) {
                Timber.d("Persisted photo with ID $id")
            }

            override fun onError(error: Throwable) {
                Timber.d("Error persisting photo: $error")
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
        val disposable = localDataStore.photosDao().insert(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(persistPhotoObserver)
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}