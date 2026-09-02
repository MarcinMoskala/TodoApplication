package com.marcinmoskala.todoapplication.domain.repositories

import com.marcinmoskala.todoapplication.domain.data.TodoItem

interface TodoItemRepository {
    suspend fun getTodoItems(): List<TodoItem>
    suspend fun addItem(newItem: TodoItem)
    suspend fun removeItem(id: String)
    suspend fun updateItem(item: TodoItem)
}