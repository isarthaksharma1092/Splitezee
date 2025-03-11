package com.isarthaksharma.splitezee.di

import android.content.Context
import androidx.room.Room
import com.isarthaksharma.splitezee.localStorage.dao.DaoGroup
import com.isarthaksharma.splitezee.localStorage.dao.DaoPersonal
import com.isarthaksharma.splitezee.localStorage.dataBase.DatabaseGroup
import com.isarthaksharma.splitezee.localStorage.dataBase.DatabasePersonal
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
    fun provideDatabase(@ApplicationContext context: Context): DatabasePersonal {
        return Room.databaseBuilder(
            context.applicationContext,
            DatabasePersonal::class.java,
            "Personal_DATABASE"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(database: DatabasePersonal): DaoPersonal {
        return database.DaoPersonal()
    }

    @Provides
    @Singleton
    fun provideGroupDatabase(@ApplicationContext context: Context): DatabaseGroup {
        return Room.databaseBuilder(
            context.applicationContext,
            DatabaseGroup::class.java,
            "Group_DATABASE"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDaoGroup(database: DatabaseGroup): DaoGroup {
        return database.DaoGroup()
    }
}