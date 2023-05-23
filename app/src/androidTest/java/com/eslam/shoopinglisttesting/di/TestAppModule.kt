package com.eslam.shoopinglisttesting.di

import android.app.Application
import androidx.room.Room
import com.eslam.shoopinglisttesting.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Named("test_db")
    @Provides
    fun provideInMemoryDb(application: Application) =
        Room.inMemoryDatabaseBuilder(context = application, ShoppingItemDatabase::class.java)
            .allowMainThreadQueries().build()
}