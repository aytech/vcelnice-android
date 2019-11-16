package cz.vcelnicerudna.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.vcelnicerudna.models.PricesData

@Dao
interface PricesDao {
    @Query("SELECT * FROM prices")
    fun getPrices(): PricesData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(prices: PricesData)
}