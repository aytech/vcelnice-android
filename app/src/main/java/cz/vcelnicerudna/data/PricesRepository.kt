package cz.vcelnicerudna.data

import cz.vcelnicerudna.data.model.Reservation

interface PricesRepository {
    fun postReservation(reservation: Reservation)
}