package cz.vcelnicerudna.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.vcelnicerudna.models.Price
import io.reactivex.Single

@Dao
interface PricesDao {
    @Query("SELECT * FROM prices")
    fun getPrices(): Single<List<Price>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(price: Price): Single<Long>
}