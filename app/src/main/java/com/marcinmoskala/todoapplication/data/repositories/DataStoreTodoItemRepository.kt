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
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "todo_items",
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, "todo_items"))
    }
)

class DataStoreTodoItemRepository(
    private val dataStore: DataStore<Preferences>
) : TodoItemRepository {

    constructor(context: Context) : this(context.dataStore)

    companion object {
        val TODO_ITEMS_KEY = stringPreferencesKey("todo_items")
    }

    override suspend fun getTodoItems(): List<TodoItem> {
        val preferences = dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .first()
        val jsonString = preferences[TODO_ITEMS_KEY] ?: return Initial
        return try {
            Json.decodeFromString<List<TodoItem>>(jsonString)
        } catch (e: Exception) {
            Initial
        }
    }

    override suspend fun addItem(text: String): TodoItem {
        var createdItem: TodoItem? = null
        dataStore.edit { preferences ->
            val currentItems = getCurrentItems(preferences)
            val nextId = (currentItems.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
            val newItem = TodoItem(
                id = nextId.toString(),
                isChecked = false,
                isFavorite = false,
                text = text
            )
            createdItem = newItem
            val updatedItems = currentItems + newItem
            preferences[TODO_ITEMS_KEY] = Json.encodeToString(updatedItems)
        }
        return createdItem ?: TodoItem(
            id = "1",
            isChecked = false,
            isFavorite = false,
            text = text
        )
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
