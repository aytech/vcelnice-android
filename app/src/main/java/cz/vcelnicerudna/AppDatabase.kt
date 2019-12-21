package cz.vcelnicerudna

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import cz.vcelnicerudna.configuration.AppConstants
import cz.vcelnicerudna.interfaces.*
import cz.vcelnicerudna.models.*

// https://medium.com/mindorks/android-architecture-components-room-and-kotlin-f7b725c8d1d
// https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
@Database(entities = [
    HomeText::class,
    NewsData::class,
    PricesData::class,
    LocationData::class,
    PhotoData::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun homeDao(): HomeDao
    abstract fun newsDao(): NewsDao
    abstract fun pricesDao(): PricesDao
    abstract fun locationsDao(): LocationsDao
    abstract fun photoDao(): PhotoDao

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