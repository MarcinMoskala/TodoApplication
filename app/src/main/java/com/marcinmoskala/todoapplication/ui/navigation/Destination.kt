package com.marcinmoskala.todoapplication.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.marcinmoskala.todoapplication.domain.data.TodoItem
import kotlinx.serialization.Serializable

sealed class Destination: NavKey {
    @Serializable
    data object Todos: Destination()
    @Serializable
    data class TodoDetails(val todo: TodoItem): Destination()
}