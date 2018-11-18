package cz.vcelnicerudna

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import cz.vcelnicerudna.configuration.AppConstants
import cz.vcelnicerudna.interfaces.HomeDao
import cz.vcelnicerudna.interfaces.LocationsDao
import cz.vcelnicerudna.interfaces.NewsDao
import cz.vcelnicerudna.interfaces.PricesDao
import cz.vcelnicerudna.models.HomeText
import cz.vcelnicerudna.models.LocationData
import cz.vcelnicerudna.models.NewsData
import cz.vcelnicerudna.models.PricesData

// https://medium.com/mindorks/android-architecture-components-room-and-kotlin-f7b725c8d1d
// https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
@Database(entities = [
    HomeText::class,
    NewsData::class,
    PricesData::class,
    LocationData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun homeDao(): HomeDao
    abstract fun newsDao(): NewsDao
    abstract fun pricesDao(): PricesDao
    abstract fun locationsDao(): LocationsDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, AppConstants.APP_DB_NAME).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}