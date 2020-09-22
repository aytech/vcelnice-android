package cz.vcelnicerudna.main

import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.data.Repository
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.News
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainPresenter(
        private var activity: MainContract.ViewInterface,
        private var repository: Repository,
        private var localDataStore: AppDatabase) : MainContract.PresenterInterface {

    private var allNews: List<News> = listOf()
    private val compositeDisposable = CompositeDisposable()

    private val homeTextObservable: Observable<HomeText>
        get() = repository.getHomeText()
    private val homeTextObserver: DisposableObserver<HomeText>
        get() = object : DisposableObserver<HomeText>() {
            override fun onNext(text: HomeText) {
                activity.showHomeText(text)
                persistHomeText(text)
            }

            override fun onError(e: Throwable) {
                activity.onHomeTextNetworkError()
            }

            override fun onComplete() {
                activity.onLoadingComplete()
            }
        }

    private val newsObservable: Observable<List<News>>
        get() = repository.getNews()
    private val newsObserver: DisposableObserver<List<News>>
        get() = object : DisposableObserver<List<News>>() {
            override fun onNext(news: List<News>) {
                activity.showNews(getSlicedNews(news))
                allNews = news
                news.forEach { persistNews(it) }
            }

            override fun onError(error: Throwable) {
                activity.onNewsNetworkError(error)
            }

            override fun onComplete() {
                activity.onLoadingComplete()
            }
        }

    private val localHomeTextDataStoreObservable: Single<HomeText>
        get() = localDataStore.homeDao().getHomeText()
    private val localHomeTextDataStoreObserver: DisposableSingleObserver<HomeText>
        get() = object : DisposableSingleObserver<HomeText>() {
            override fun onSuccess(text: HomeText) {
                activity.showHomeText(text)
            }

            override fun onError(error: Throwable) {
                activity.showError()
            }
        }

    private val localNewsDataStoreObservable: Single<List<News>>
        get() = localDataStore.newsDao().getNews()
    private val localNewsDataStoreObserver: DisposableSingleObserver<List<News>>
        get() = object : DisposableSingleObserver<List<News>>() {
            override fun onSuccess(news: List<News>) {
                activity.showNews(getSlicedNews(news))
                allNews = news
            }

            override fun onError(error: Throwable) {
                activity.showError()
            }
        }

    private val persistHomeTextObserver: DisposableSingleObserver<Long>
        get() = object : DisposableSingleObserver<Long>() {
            override fun onSuccess(id: Long) {
                Timber.d("Persisted home text with ID $id")
            }

            override fun onError(error: Throwable) {
                Timber.d("Error persisting home text, $error")
            }
        }

    private val persistNewsObserver: DisposableSingleObserver<Long>
        get() = object : DisposableSingleObserver<Long>() {
            override fun onSuccess(id: Long) {
                Timber.d("Persisted home text with ID $id")
            }

            override fun onError(error: Throwable) {
                Timber.d("Error persisting home text, $error")
            }
        }

    fun getSlicedNews(news: List<News>): List<News> {
        val sortedList = news.sortedByDescending { it.updated }
        val lastIndex = if (news.size >= 3) 2 else news.size - 1
        return sortedList.slice(0..lastIndex)
    }

    override fun fetchHomeTextFromApi() {
        val homeTextDisposable = homeTextObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(homeTextObserver)
        compositeDisposable.add(homeTextDisposable)
    }

    override fun fetchNewsFromApi() {
        val newsDisposable = newsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(newsObserver)
        compositeDisposable.add(newsDisposable)
    }

    override fun fetchHomeTextFromLocalDataStore() {
        val localStoreDisposable = localHomeTextDataStoreObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(localHomeTextDataStoreObserver)
        compositeDisposable.add(localStoreDisposable)
    }

    override fun fetchNewsFromLocalDataStore() {
        val localNewsDisposable = localNewsDataStoreObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(localNewsDataStoreObserver)
        compositeDisposable.add(localNewsDisposable)
    }

    override fun getNews(): List<News> = allNews

    override fun persistHomeText(text: HomeText) {
        val disposable = localDataStore.homeDao().insert(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(persistHomeTextObserver)
        compositeDisposable.add(disposable)
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