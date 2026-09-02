package com.marcinmoskala.todoapplication.ui.todos.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.todoapplication.ui.theme.TodoApplicationTheme
import com.marcinmoskala.todoapplication.ui.todos.TodoItem

@Composable
fun TodoItem(
    item: TodoItem,
    modifier: Modifier = Modifier,
    onToggleCheckbox: (Boolean) -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggleCheckbox(it) }
            )
            Text(
                text = item.text,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.clickable { onDelete() }
                    .padding(end = 10.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoItemPreview() {
    TodoApplicationTheme {
        TodoItem(
            item = TodoItem(
                id = "1",
                isChecked = true,
                isFavorite = true,
                text = "Test"
            ),
            onDelete = {}
        )
    }
}