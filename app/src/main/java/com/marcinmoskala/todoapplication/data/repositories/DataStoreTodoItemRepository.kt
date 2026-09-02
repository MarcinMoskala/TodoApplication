package com.marcinmoskala.todoapplication.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.domain.repositories.TodoItemRepository
import com.marcinmoskala.todoapplication.ui.todos.Initial
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "todo_items",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, "todo_items"))
    }
)

@Singleton
class DataStoreTodoItemRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TodoItemRepository {

    constructor(context: Context) : this(context.dataStore)

    companion object {
        val TODO_ITEMS_KEY = stringPreferencesKey("todo_items")
    }

    override fun observeTodoItems() = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val jsonString = preferences[TODO_ITEMS_KEY] ?: return@map Initial
            try {
                Json.decodeFromString<List<TodoItem>>(jsonString)
            } catch (e: Exception) {
                Initial
            }
        }
        .distinctUntilChanged()

    override suspend fun getTodoItems(): List<TodoItem> =
        observeTodoItems().first()

    override suspend fun addItem(newItem: TodoItem) {
        dataStore.edit { preferences ->
            val currentItems = getCurrentItems(preferences)
            val updatedItems = currentItems + newItem
            preferences[TODO_ITEMS_KEY] = Json.encodeToString(updatedItems)
        }
    }

    override suspend fun removeItem(id: String) {
        dataStore.edit { preferences ->
            val currentItems = getCurrentItems(preferences)
            val updatedItems = currentItems.filter { it.id != id }
            preferences[TODO_ITEMS_KEY] = Json.encodeToString(updatedItems)
        }
    }

    override suspend fun updateItem(item: TodoItem) {
        dataStore.edit { preferences ->
            val currentItems = getCurrentItems(preferences)
            val updatedItems = currentItems.map { if (it.id == item.id) item else it }
            preferences[TODO_ITEMS_KEY] = Json.encodeToString(updatedItems)
        }
    }

    private fun getCurrentItems(preferences: Preferences): List<TodoItem> {
        val jsonString = preferences[TODO_ITEMS_KEY] ?: return Initial
        return try {
            Json.decodeFromString<List<TodoItem>>(jsonString)
        } catch (e: Exception) {
            Initial
        }
    }
}

typealias TodoItemRepositoryImpl = DataStoreTodoItemRepository
typealias SharedPreferencesTodoItemRepository = DataStoreTodoItemRepository
