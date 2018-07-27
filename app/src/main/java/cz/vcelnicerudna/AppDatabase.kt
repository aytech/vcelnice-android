package cz.vcelnicerudna

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import cz.vcelnicerudna.interfaces.HomeDao
import cz.vcelnicerudna.models.HomeText

@Database(entities = [HomeText::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun homeDao(): HomeDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "vcelnice.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}