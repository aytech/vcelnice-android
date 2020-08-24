package cz.vcelnicerudna.reserve

import cz.vcelnicerudna.models.Location

class ReserveContract {
    interface PresenterInterface {
        fun fetchLocationsFromAPI()
        fun fetchLocationsFromLocalDataStore()
        fun persistLocation(location: Location)
    }

    interface ViewInterface {
        fun showLocations(locations: List<Location>)
        fun onNetworkError()
    }
}