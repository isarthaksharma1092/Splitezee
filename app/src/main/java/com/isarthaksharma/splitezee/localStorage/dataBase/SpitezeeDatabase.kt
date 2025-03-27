package com.isarthaksharma.splitezee.localStorage.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroup
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroupDetails
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroupExpense
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroupMember
import com.isarthaksharma.splitezee.localStorage.dao.DaoPersonal
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDetailDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupExpenseDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass

@Database(
    entities = [
        PersonalDataClass::class,
        GroupDataClass::class,
        GroupDetailDataClass::class,
        GroupMemberDataClass::class,
        GroupExpenseDataClass::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverterClass::class)
abstract class SplitezeeDatabase : RoomDatabase() {

    abstract fun daoPersonal(): DaoPersonal
    abstract fun daoGroupDetails(): DaoGroupDetails
    abstract fun daoGroupMember(): DaoGroupMember
    abstract fun daoGroupExpense(): DaoGroupExpense
    abstract fun daoGroup(): DaoGroup

    companion object {
        @Volatile
        private var INSTANCE: SplitezeeDatabase? = null

        fun getDatabase(context: Context): SplitezeeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SplitezeeDatabase::class.java,
                    "Splitezee_DATABASE"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
