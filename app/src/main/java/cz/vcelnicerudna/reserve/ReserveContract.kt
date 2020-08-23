package cz.vcelnicerudna.reserve

import cz.vcelnicerudna.models.Location

class ReserveContract {
    interface PresenterInterface {
        fun fetchLocationsFromAPI()
        fun fetchLocationsFromLocalDataStore()
        fun persistLocation()
    }

    interface ViewInterface {
        fun showLocations(locations: List<Location>)
    }
}