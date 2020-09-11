package cz.vcelnicerudna.reserve

import cz.vcelnicerudna.data.model.Reservation
import cz.vcelnicerudna.models.Location

class ReserveContract {
    interface PresenterInterface {
        fun fetchLocationsFromApi()
        fun fetchLocationsFromLocalDataStore()
        fun persistLocation(location: Location)
        fun postReservation(reservation: Reservation)
    }

    interface ViewInterface {
        fun loadingComplete()
        fun onFailPostReservation()
        fun onNetworkError()
        fun onSuccessPostReservation()
        fun postReservation()
        fun showDefaultLocation()
        fun showLocations(locations: List<Location>?)
    }
}