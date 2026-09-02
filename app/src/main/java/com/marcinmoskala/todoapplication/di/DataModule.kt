package com.marcinmoskala.todoapplication.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.marcinmoskala.todoapplication.data.repositories.DataStoreTodoItemRepository
import com.marcinmoskala.todoapplication.data.repositories.dataStore
import com.marcinmoskala.todoapplication.domain.repositories.TodoItemRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTodoItemRepository(
        dataStoreTodoItemRepository: DataStoreTodoItemRepository
    ): TodoItemRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore
}
