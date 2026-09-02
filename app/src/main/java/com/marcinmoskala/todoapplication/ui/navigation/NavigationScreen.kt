package com.marcinmoskala.todoapplication.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.marcinmoskala.todoapplication.ui.details.Navigator
import com.marcinmoskala.todoapplication.ui.details.TodoDetailsScreen
import com.marcinmoskala.todoapplication.ui.todos.TodoScreen

@Composable
fun NavigationScreen(
    innerPadding: PaddingValues,
    navigator: Navigator,
) {
    NavDisplay(
        modifier = Modifier.padding(innerPadding),
        backStack = navigator.navBackStack,
        onBack = { navigator.navigateBack() },
        entryProvider = entryProvider {
            entry<Destination.Todos> {
                TodoScreen()
            }
            entry<Destination.TodoDetails> { destination ->
                TodoDetailsScreen(
                    item = destination.todo,
                    onBack = {
                        navigator.navigateBack()
                    }
                )
            }
        }
    )
}