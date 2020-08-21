package cz.vcelnicerudna.main

import cz.vcelnicerudna.models.HomeText

class MainContract {
    interface PresenterInterface {
        fun fetchHomeTextFromApi()
        fun fetchHomeTextFromLocalDataStore()
        fun onDestroy()
    }

    interface ViewInterface {
        fun showHomeText(text: HomeText)
        fun onNetworkError()
        fun showError()
    }
}