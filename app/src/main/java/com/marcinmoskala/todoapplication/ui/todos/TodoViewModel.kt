package com.marcinmoskala.todoapplication.ui.todos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.domain.repositories.TodoItemRepository
import com.marcinmoskala.todoapplication.domain.usecase.AddItemUseCase
import com.marcinmoskala.todoapplication.ui.details.Navigator
import com.marcinmoskala.todoapplication.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import perfetto.protos.UiState
import javax.inject.Inject
import kotlin.collections.map

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val itemRepository: TodoItemRepository,
    private val addItemUseCase: AddItemUseCase,
    private val navigator: Navigator,
) : ViewModel() {
    private val items = itemRepository.observeTodoItems().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList(),
    )
    private val dialogState = MutableStateFlow<String?>(null)

    val uiState = items.combine(dialogState) { items, dialogState ->
        TodoUiState(
            items = items,
            addDialog = dialogState,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        TodoUiState(emptyList(), null)
    )

    fun onAction(uiAction: TodoUiAction) {
        when (uiAction) {
            is TodoUiAction.AddItem -> addItem(uiAction.text)
            TodoUiAction.AddItemClicked -> onAddItemClicked()
            is TodoUiAction.DeleteItem -> onDeleteItem(uiAction.itemId)
            TodoUiAction.DismissAddItemDialog -> onDismissAddItemDialog()
            is TodoUiAction.EditAddItemDialogText -> onEditAddItemDialogText(uiAction.text)
            is TodoUiAction.ItemClicked -> onItemClicked(uiAction.item)
            is TodoUiAction.ToggleCheckbox -> onToggleCheckbox(uiAction.itemId, uiAction.newState)
            is TodoUiAction.ToggleFavorite -> onToggleFavorite(uiAction.todoId)
        }
    }

    private fun onToggleCheckbox(itemId: String, newState: Boolean) {
        viewModelScope.launch {
            val item = items.value.firstOrNull { it.id == itemId } ?: return@launch
            itemRepository.updateItem(item.copy(isChecked = newState))
        }
    }

    private fun onDeleteItem(itemId: String) {
        viewModelScope.launch {
            itemRepository.removeItem(itemId)
        }
    }

    private fun addItem(text: String) {
        if (text.isNotEmpty()) {
            dialogState.value = null
            viewModelScope.launch {
                addItemUseCase(text)
            }
        }
    }

    private fun onAddItemClicked() {
        dialogState.value = ""
    }

    private fun onDismissAddItemDialog() {
        dialogState.value = null
    }

    private fun onEditAddItemDialogText(text: String) {
        dialogState.value = text
    }

    private fun onToggleFavorite(itemId: String) {
        viewModelScope.launch {
            val item = items.value.firstOrNull { it.id == itemId } ?: return@launch
            val updated = item.copy(isFavorite = !item.isFavorite)
            itemRepository.updateItem(updated)
        }
    }

    private fun onItemClicked(item: TodoItem) {
        navigator.navigateTo(Destination.TodoDetails(item))
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