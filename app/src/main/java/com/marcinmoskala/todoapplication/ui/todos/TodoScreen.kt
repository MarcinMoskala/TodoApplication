package com.marcinmoskala.todoapplication.ui.todos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcinmoskala.todoapplication.ui.TodoAppPreview
import com.marcinmoskala.todoapplication.ui.theme.TodoApplicationTheme
import com.marcinmoskala.todoapplication.ui.todos.components.AddItemDialog
import com.marcinmoskala.todoapplication.ui.todos.components.TodoItem
import kotlinx.coroutines.delay

@Composable
fun TodoScreen(
    modifier: Modifier = Modifier,
    vm: TodoViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    TodoScreen(
        state = state,
        onAction = vm::onAction,
        modifier = modifier
    )
}



@Composable
fun TodoScreen(
    state: TodoUiState,
    onAction: (TodoUiAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onAction(TodoUiAction.AddItemClicked)
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
                onAction = onAction,
            )
        }

    }
    if (state.addDialog != null) {
        AddItemDialog(onAction, state.addDialog)
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
    state: TodoUiState,
    onAction: (TodoUiAction.Content) -> Unit
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
                onAction = onAction,
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onAction(TodoUiAction.ItemClicked(item)) }
            )
        }
    }
}

@TodoAppPreview
@Composable
fun TodoScreenPreview(
    @PreviewParameter(TodoScreenUiStateProvider::class)
    state: TodoUiState
) {
    TodoApplicationTheme {
        TodoScreen(
            state = state,
            onAction = {},
        )
    }
}
