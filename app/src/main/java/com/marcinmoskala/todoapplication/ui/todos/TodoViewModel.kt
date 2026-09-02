package com.marcinmoskala.todoapplication.ui.todos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.collections.map

class TodoViewModel {
    var items by mutableStateOf(Initial)
    var addDialog by mutableStateOf<String?>(null)

    fun onToggleCheckbox(itemId: String, newState: Boolean) {
        items = items.map { if (it.id == itemId) it.copy(isChecked = newState) else it }
    }

    fun onDeleteItem(itemId: String) {
        items = items.filter { if (itemId == it.id) false else true }
    }

    fun addItem(text: String) {
        if (text.isNotEmpty()) {
            val nextId = (items.maxOfOrNull { it.id.toIntOrNull() ?: 0 } ?: 0) + 1
            items = items + TodoItem(
                id = nextId.toString(),
                isChecked = false,
                isFavorite = false,
                text = text
            )
            addDialog = null
        }
    }

    fun onAddItemClicked() {
        addDialog = ""
    }

    fun onDismissAddItemDialog() {
        addDialog = null
    }
}

data class TodoItem(
    val id: String,
    val isChecked: Boolean,
    val isFavorite: Boolean,
    val text: String,
)

private val Initial = listOf(
    TodoItem(
        id = "1",
        isChecked = false,
        isFavorite = false,
        text = "Buy groceries",
    ),
    TodoItem(
        id = "2",
        isChecked = true,
        isFavorite = true,
        text = "Learn Kotlin and Jetpack Compose",
    ),
    TodoItem(
        id = "3",
        isChecked = false,
        isFavorite = true,
        text = "Go for a morning run",
    ),
    TodoItem(
        id = "4",
        isChecked = false,
        isFavorite = false,
        text = "Read a chapter of a book",
    ),
)