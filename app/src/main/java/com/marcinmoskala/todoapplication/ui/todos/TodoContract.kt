package com.marcinmoskala.todoapplication.ui.todos

import com.marcinmoskala.todoapplication.domain.data.TodoItem

data class TodoUiState(
    val items: List<TodoItem>,
    val addDialog: String?,
)

sealed interface TodoUiAction {
    data object AddItemClicked : TodoUiAction

    sealed interface Dialog: TodoUiAction
    data class AddItem(val text: String) : Dialog
    data object DismissAddItemDialog : Dialog
    data class EditAddItemDialogText(val text: String) : Dialog

    sealed interface Content: TodoUiAction
    data class ToggleCheckbox(val itemId: String, val newState: Boolean) : Content
    data class DeleteItem(val itemId: String) : Content
    data class ToggleFavorite(val todoId: String) : Content
    data class ItemClicked(val item: TodoItem) : Content
}