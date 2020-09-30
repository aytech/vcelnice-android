package cz.vcelnicerudna.prices

import cz.vcelnicerudna.data.model.Price

class PricesContract {
    interface PresenterInterface {
        fun fetchPricesFromApi()
        fun fetchPricesFromLocalDataStore()
        fun persistPrice(price: Price)
        fun onDestroy()
    }

    interface ViewInterface {
        fun onNetworkError()
        fun showError()
        fun showPrices(prices: List<Price>)
        fun onReserveClicked(price: Price)
        fun onPricesLoaded()
    }
}