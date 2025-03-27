package com.isarthaksharma.splitezee.di

import android.content.Context
import androidx.room.Room
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroup
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroupDetails
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroupExpense
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroupMember
import com.isarthaksharma.splitezee.localStorage.dao.DaoPersonal
import com.isarthaksharma.splitezee.localStorage.dataBase.SplitezeeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomInstance {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SplitezeeDatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            SplitezeeDatabase::class.java,
            "Splitezee_DATABASE"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(database: SplitezeeDatabase): DaoPersonal {
        return database.daoPersonal()
    }

    @Provides
    @Singleton
    fun provideGroupDao(database: SplitezeeDatabase): DaoGroup {
        return database.daoGroup()
    }

    @Provides
    @Singleton
    fun provideDaoGroupMember(database: SplitezeeDatabase): DaoGroupMember {
        return database.daoGroupMember()
    }

    @Provides
    @Singleton
    fun provideDaoGroupDetails(database: SplitezeeDatabase): DaoGroupDetails {
        return database.daoGroupDetails()
    }

    @Provides
    @Singleton
    fun provideDaoGroupExpense(database: SplitezeeDatabase): DaoGroupExpense {
        return database.daoGroupExpense()
    }
}