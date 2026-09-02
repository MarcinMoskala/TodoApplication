package com.marcinmoskala.todoapplication.ui.todos

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.marcinmoskala.todoapplication.data.repositories.DataStoreTodoItemRepository
import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.ui.TodoAppPreview
import com.marcinmoskala.todoapplication.ui.theme.TodoApplicationTheme
import com.marcinmoskala.todoapplication.ui.todos.components.TodoItem
import kotlinx.coroutines.delay

@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    vm: TodoViewModel = run {
        val context = LocalContext.current
        remember { TodoViewModel(DataStoreTodoItemRepository(context)) }
    }
) {
    val items = vm.items
    val addDialog = vm.addDialog

    TodoScreen(
        state = TodoScreenUiState(items, addDialog),
        onAddItemClicked = vm::onAddItemClicked,
        onDeleteItem = vm::onDeleteItem,
        onToggleCheckbox = vm::onToggleCheckbox,
        onToggleFavorite = vm::onToggleFavorite,
        onDismissAddItemDialog = vm::onDismissAddItemDialog,
        onEditAddItemDialogText = vm::onEditAddItemDialogText,
        addItem = vm::addItem,
        modifier = modifier
    )
}

data class TodoScreenUiState(
    val items: List<TodoItem>,
    val addDialog: String?,
)

@Composable
fun TodoScreen(
    state: TodoScreenUiState,
    onAddItemClicked: () -> Unit,
    onDeleteItem: (String) -> Unit,
    onToggleCheckbox: (String, Boolean) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onDismissAddItemDialog: () -> Unit,
    onEditAddItemDialogText: (String) -> Unit,
    addItem: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onAddItemClicked()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        when {
            state.items.isEmpty() -> TodoEmptyState()
            else -> TodoContent(
                modifier = modifier,
                innerPadding = innerPadding,
                state = state,
                onDeleteItem = onDeleteItem,
                onToggleCheckbox = onToggleCheckbox,
                onToggleFavorite = onToggleFavorite
            )
        }

    }
    if (state.addDialog != null) {
        AlertDialog(
            onDismissRequest = onDismissAddItemDialog,
            title = { Text(text = "Add Item") },
            text = {
                TextField(
                    value = state.addDialog ?: "",
                    onValueChange = onEditAddItemDialogText,
                    label = { Text("Item text") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val text = state.addDialog.trim()
                        addItem(text)
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = onDismissAddItemDialog) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TodoEmptyState() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            "No items",
            modifier = Modifier.align(Alignment.Center),
            fontSize = MaterialTheme.typography.headlineLarge.fontSize
        )
    }
}

@Composable
private fun TodoContent(
    modifier: Modifier,
    innerPadding: PaddingValues,
    state: TodoScreenUiState,
    onDeleteItem: (String) -> Unit,
    onToggleCheckbox: (String, Boolean) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(state.items.lastOrNull()?.id) {
        delay(400)
        listState.animateScrollToItem(listState.layoutInfo.totalItemsCount - 1)
    }
    LazyColumn(
        state = listState,
        modifier = modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        items(state.items, key = { it.id }) { item ->
            TodoItem(
                item,
                onDelete = { onDeleteItem(item.id) },
                onToggleCheckbox = { newState -> onToggleCheckbox(item.id, newState) },
                onFavoriteToggle = { onToggleFavorite(item.id) },
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@TodoAppPreview
@Composable
fun TodoScreenPreview(
    @PreviewParameter(TodoScreenUiStateProvider::class)
    state: TodoScreenUiState
) {
    TodoApplicationTheme {
        TodoScreen(
            state = state,
            onAddItemClicked = {},
            onDeleteItem = {},
            onToggleCheckbox = { _, _ -> },
            onEditAddItemDialogText = {},
            onToggleFavorite = {},
            onDismissAddItemDialog = {},
            addItem = {},
        )
    }
}

@TodoAppPreview
@Composable
private fun ActiveTodoScreenPreview() {
    TodoApplicationTheme {
        TodoScreen()
    }
}