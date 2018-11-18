package cz.vcelnicerudna.interfaces

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import cz.vcelnicerudna.models.PricesData

@Dao
interface PricesDao {
    @Query("SELECT * FROM prices")
    fun getPrices(): PricesData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(prices: PricesData)
}