package cz.vcelnicerudna.news

import android.util.Log
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.models.News
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class NewsPresenter (
        private var activity: NewsContract.ViewInterface,
        private var repository: Repository,
        private var localDataStore: AppDatabase) : NewsContract.PresenterInterface {

    private val classTag = NewsPresenter::class.simpleName
    private val compositeDisposable = CompositeDisposable()

    private val apiObservable: Observable<List<News>>
        get() = repository.getNews()
    private val apiObserver: DisposableObserver<List<News>>
        get() = object : DisposableObserver<List<News>>() {
            override fun onNext(news: List<News>) {
                activity.showNews(news)
                news.forEach {
                    persistNews(it)
                }
            }

            override fun onError(e: Throwable) {
                activity.onNetworkError()
            }

            override fun onComplete() {
                Log.d(classTag, "Finished loading news")
            }
        }

    private val localDataStoreObservable: Single<List<News>>
        get() = localDataStore.newsDao().getNews()
    private val localDataStoreObserver: DisposableSingleObserver<List<News>>
        get() = object : DisposableSingleObserver<List<News>>() {
            override fun onSuccess(news: List<News>) {
                activity.showNews(news)
            }

            override fun onError(e: Throwable) {
                activity.showError()
            }
        }

    private val persistNewsObserver: DisposableSingleObserver<Long>
        get() = object : DisposableSingleObserver<Long>() {
            override fun onSuccess(id: Long) {
                Log.d(classTag, "inserted to DB with ids: $id")
            }

            override fun onError(e: Throwable) {
                Log.d(classTag, "Error persisting news: $e")
            }
        }

    override fun fetchNewsFromAPI() {
        val newsDisposable = apiObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(apiObserver)
        compositeDisposable.add(newsDisposable)
    }

    override fun fetchNewsFromLocalDataStore() {
        val localStoreDisposable = localDataStoreObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(localDataStoreObserver)
        compositeDisposable.add(localStoreDisposable)
    }

    override fun persistNews(news: News) {
        val disposable = localDataStore.newsDao().insert(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(persistNewsObserver)
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}