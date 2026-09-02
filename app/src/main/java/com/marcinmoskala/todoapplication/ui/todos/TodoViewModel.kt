package com.marcinmoskala.todoapplication.ui.todos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.domain.repositories.TodoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.collections.map

class TodoViewModel(
    private val itemRepository: TodoItemRepository,
) {
    val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    var items by mutableStateOf(Initial)
    var addDialog by mutableStateOf<String?>(null)

    init {
        scope.launch {
            items = itemRepository.getTodoItems()
        }
    }

    fun onToggleCheckbox(itemId: String, newState: Boolean) {
        scope.launch {
            val item = items.firstOrNull { it.id == itemId } ?: return@launch
            itemRepository.updateItem(item.copy(isChecked = newState))
            items = items.map { if (it.id == itemId) it.copy(isChecked = newState) else it }
        }
    }

    fun onDeleteItem(itemId: String) {
        scope.launch {
            itemRepository.removeItem(itemId)
            items = items.filter { if (itemId == it.id) false else true }
        }
    }

    fun addItem(text: String) {
        if (text.isNotEmpty()) {
            addDialog = null
            scope.launch {
                val newItem = itemRepository.addItem(text)
                items = items + newItem
            }
        }
    }

    fun onAddItemClicked() {
        addDialog = ""
    }

    fun onDismissAddItemDialog() {
        addDialog = null
    }

    fun onEditAddItemDialogText(text: String) {
        addDialog = text
    }

    fun onToggleFavorite(todoId: String) {
        scope.launch {
            val item = items.firstOrNull { it.id == todoId } ?: return@launch
            val updated = item.copy(isFavorite = !item.isFavorite)
            itemRepository.updateItem(updated)
            items = items.map { if (it.id == todoId) updated else it }
        }
    }
}



val Initial = listOf(
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