package com.marcinmoskala.todoapplication.ui.todos.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.ui.theme.TodoApplicationTheme
import com.marcinmoskala.todoapplication.ui.todos.TodoUiAction

@Composable
fun TodoItem(
    item: TodoItem,
    modifier: Modifier = Modifier,
    onAction: (TodoUiAction.Content) -> Unit,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (item.isFavorite) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
            contentColor = if (item.isFavorite) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onAction(TodoUiAction.ToggleCheckbox(item.id, it)) }
            )
            Text(
                text = item.text,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onAction(TodoUiAction.ToggleFavorite(item.id)) }) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                )
            }
            IconButton(onClick = { onAction(TodoUiAction.DeleteItem(item.id)) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(showBackground = false,
    device = "spec:width=480px,height=800px,dpi=240,isRound=true,navigation=buttons", showSystemUi = false,
    uiMode = Configuration.UI_MODE_TYPE_CAR
)
@Composable
private fun TodoItemPreview(text: String = "AAA") {
    TodoApplicationTheme {
        TodoItem(
            item = TodoItem(
                id = "1",
                isChecked = true,
                isFavorite = false,
                text = "Test"
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoItem2Preview() {
    TodoApplicationTheme {
        TodoItem(
            item = TodoItem(
                id = "1",
                isChecked = false,
                isFavorite = true,
                text = "Test"
            ),
            onAction = {},
        )
    }
}