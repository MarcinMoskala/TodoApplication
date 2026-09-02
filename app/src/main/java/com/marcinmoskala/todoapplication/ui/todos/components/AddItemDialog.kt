package com.marcinmoskala.todoapplication.ui.todos.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.marcinmoskala.todoapplication.ui.todos.TodoUiAction

@Composable
fun AddItemDialog(
    onAction: (TodoUiAction.Dialog) -> Unit,
    addDialog: String
) {
    AlertDialog(
        onDismissRequest = { onAction(TodoUiAction.DismissAddItemDialog) },
        title = { Text(text = "Add Item") },
        text = {
            TextField(
                value = addDialog ?: "",
                onValueChange = { onAction(TodoUiAction.EditAddItemDialogText(it)) },
                label = { Text("Item text") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val text = addDialog.trim()
                    onAction(TodoUiAction.AddItem(text))
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = { onAction(TodoUiAction.DismissAddItemDialog) }) {
                Text("Cancel")
            }
        }
    )
}