package cz.vcelnicerudna.photo

import cz.vcelnicerudna.data.model.Photo

class PhotoContract {
    interface PresenterInterface {
        fun fetchPhotosFromAPI()
        fun fetchPhotosFromLocalDataStore()
        fun persistPhotos(photo: Photo)
        fun onDestroy()
    }

    interface ViewInterface {
        fun showPhotos(photos: List<Photo>)
        fun onNetworkError()
        fun onPhotosLoaded()
        fun showError()
    }
}