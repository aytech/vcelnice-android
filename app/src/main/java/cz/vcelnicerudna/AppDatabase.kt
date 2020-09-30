package cz.vcelnicerudna

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import cz.vcelnicerudna.configuration.AppConstants
import cz.vcelnicerudna.data.dao.*
import cz.vcelnicerudna.data.model.*

// https://medium.com/mindorks/android-architecture-components-room-and-kotlin-f7b725c8d1d
// https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
@Database(entities = [
    HomeText::class,
    News::class,
    Price::class,
    Location::class,
    Photo::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun homeDao(): HomeDao
    abstract fun newsDao(): NewsDao
    abstract fun pricesDao(): PricesDao
    abstract fun locationsDao(): LocationsDao
    abstract fun photosDao(): PhotosDao

    companion object {
        private lateinit var INSTANCE: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, AppConstants.APP_DB_NAME)
                        .fallbackToDestructiveMigration().build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE.close()
        }
    }
}