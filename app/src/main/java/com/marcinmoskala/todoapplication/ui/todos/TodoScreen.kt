package com.marcinmoskala.todoapplication.ui.todos

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.todoapplication.ui.theme.TodoApplicationTheme
import com.marcinmoskala.todoapplication.ui.todos.components.TodoItem

@Composable
fun TodoScreen(modifier: Modifier = Modifier) {
    val vm = remember { TodoViewModel() }
    val items = vm.items
    val addDialog = vm.addDialog

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                vm.onAddItemClicked()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(items, key = { it.id }) { item ->
                TodoItem(
                    item,
                    onDelete = {
                        vm.onDeleteItem(item.id)
                    },
                    onToggleCheckbox = { newState ->
                        vm.onToggleCheckbox(item.id, newState)
                    },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
    if (addDialog != null) {
        AlertDialog(
            onDismissRequest = { vm.onDismissAddItemDialog() },
            title = { Text(text = "Add Item") },
            text = {
                TextField(
                    value = addDialog ?: "",
                    onValueChange = { vm.onDismissAddItemDialog() },
                    label = { Text("Item text") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val text = addDialog?.trim().orEmpty()
                        vm.addItem(text)
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { vm.onDismissAddItemDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoScreenPreview() {
    TodoApplicationTheme {
        TodoScreen()
    }
}