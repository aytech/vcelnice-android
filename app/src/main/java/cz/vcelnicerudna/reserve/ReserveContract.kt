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
        fun onLocationsFetchComplete()
        fun onFailPostReservation()
        fun onNetworkError()
        fun onSuccessPostReservation()
        fun onCompletePostReservation()
        fun postReservation()
        fun showDefaultLocation()
        fun showLocations(locations: List<Location>?)
    }
}