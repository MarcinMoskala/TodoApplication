package com.marcinmoskala.todoapplication.ui.todos.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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

@Composable
fun TodoItem(
    item: TodoItem,
    modifier: Modifier = Modifier,
    onFavoriteToggle: (Boolean) -> Unit = {},
    onToggleCheckbox: (Boolean) -> Unit = {},
    onDelete: () -> Unit = {}
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
                onCheckedChange = { onToggleCheckbox(it) }
            )
            Text(
                text = item.text,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onFavoriteToggle(!item.isFavorite) }) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                )
            }
            IconButton(onClick = { onDelete() }) {
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
            onDelete = {}
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
            onDelete = {}
        )
    }
}