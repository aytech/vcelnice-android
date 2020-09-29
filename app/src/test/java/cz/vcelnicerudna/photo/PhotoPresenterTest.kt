package cz.vcelnicerudna.photo

import com.nhaarman.mockitokotlin2.given
import cz.vcelnicerudna.AppDatabase
import cz.vcelnicerudna.BaseTest
import cz.vcelnicerudna.RxImmediateSchedulerRule
import cz.vcelnicerudna.interfaces.VcelniceAPI
import cz.vcelnicerudna.models.Photo
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.collections.ArrayList

@RunWith(MockitoJUnitRunner::class)
class PhotoPresenterTest : BaseTest() {
    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var mockActivity: PhotoContract.ViewInterface

    @Mock
    private lateinit var mockDataSource: VcelniceAPI

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private lateinit var mockLocalDataStore: AppDatabase

    private lateinit var photoPresenter: PhotoPresenter

    private val dummyPhotos: List<Photo>
        get() {
            val photos = ArrayList<Photo>()
            val firstPhoto = Photo()
            firstPhoto.caption = "First photo caption"
            firstPhoto.image = "/dummy/image"
            val secondPhoto = Photo()
            secondPhoto.caption = "Second photo caption"
            secondPhoto.image = "/dummy/image"

            photos.addAll(arrayListOf(firstPhoto, secondPhoto))
            return photos
        }

    @Before
    fun setUp() {
        photoPresenter = PhotoPresenter(mockActivity, mockDataSource, mockLocalDataStore)
    }

    @Test
    fun testFetchPhotosFromApi() {
        val photos = dummyPhotos
        Mockito.doReturn(Observable.just(photos)).`when`(mockDataSource).getPhotos()

        photoPresenter.fetchPhotosFromAPI()

        Mockito.verify(mockDataSource).getPhotos()
        Mockito.verify(mockActivity).showPhotos(photos)
        Mockito.verify(mockActivity).onPhotosLoaded()
        Mockito.verify(mockLocalDataStore.photosDao()).insert(photos[0])
        Mockito.verify(mockLocalDataStore.photosDao()).insert(photos[1])
    }

    @Test
    fun testFetchPhotosFromApiError() {
        Mockito.doReturn(Observable.error<Throwable>(Throwable("API fetch failed"))).`when`(mockDataSource).getPhotos()

        photoPresenter.fetchPhotosFromAPI()

        Mockito.verify(mockDataSource).getPhotos()
        Mockito.verify(mockActivity).onNetworkError()
    }

    @Test
    fun testFetchPhotosFromLocalDataStore() {
        val photos = dummyPhotos
        given(mockLocalDataStore.photosDao().getPhotos()).willReturn(Single.just(photos))

        photoPresenter.fetchPhotosFromLocalDataStore()

        Mockito.verify(mockLocalDataStore.photosDao()).getPhotos()
        Mockito.verify(mockActivity).showPhotos(photos)
    }

    @Test
    fun testPersistPhotos() {
        val photo = dummyPhotos[0]
        photoPresenter.persistPhotos(photo)

        Mockito.verify(mockLocalDataStore.photosDao()).insert(photo)
    }
}