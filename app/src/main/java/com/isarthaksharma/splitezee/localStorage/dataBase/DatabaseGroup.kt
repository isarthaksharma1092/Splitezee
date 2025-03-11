package com.isarthaksharma.splitezee.localStorage.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.isarthaksharma.splitezee.localStorage.StringListConverter
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroup
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDataClass

@Database(
    entities = [GroupDataClass::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class DatabaseGroup :RoomDatabase(){
    abstract fun DaoGroup(): DaoGroup

    companion object {
        private var INSTANCE: DatabasePersonal? = null

        fun getDatabase(context: Context): DatabasePersonal {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabasePersonal::class.java,
                    "Group_DATABASE"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}