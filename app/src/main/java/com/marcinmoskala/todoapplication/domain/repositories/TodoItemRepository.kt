package com.marcinmoskala.todoapplication.domain.repositories

import com.marcinmoskala.todoapplication.domain.data.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {
    fun observeTodoItems(): Flow<List<TodoItem>>
    suspend fun getTodoItems(): List<TodoItem>
    suspend fun addItem(newItem: TodoItem)
    suspend fun removeItem(id: String)
    suspend fun updateItem(item: TodoItem)
}