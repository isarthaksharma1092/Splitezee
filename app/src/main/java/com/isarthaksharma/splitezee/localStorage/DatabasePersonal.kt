package com.isarthaksharma.splitezee.localStorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.isarthaksharma.splitezee.dataClass.PersonalDataClass

@Database(
    entities = [PersonalDataClass::class],
    version = 1
)
abstract class DatabasePersonal : RoomDatabase() {
    abstract fun DaoPersonal(): DaoPersonal

    companion object {
        private var INSTANCE: DatabasePersonal? = null

        fun getDatabase(context: Context): DatabasePersonal {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabasePersonal::class.java,
                    "Personal_DATABASE"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}