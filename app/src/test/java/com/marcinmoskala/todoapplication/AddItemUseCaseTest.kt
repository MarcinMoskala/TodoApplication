package com.marcinmoskala.todoapplication

import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.domain.repositories.TodoItemRepository
import com.marcinmoskala.todoapplication.domain.usecase.AddItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AddItemUseCaseTest {

    private class FakeTodoItemRepository : TodoItemRepository {
        val items = mutableListOf<TodoItem>()

        override fun observeTodoItems(): Flow<List<TodoItem>> = flowOf(items.toList())

        override suspend fun getTodoItems(): List<TodoItem> = items.toList()

        override suspend fun addItem(newItem: TodoItem) {
            items.add(newItem)
        }

        override suspend fun removeItem(id: String) {
            items.removeAll { it.id == id }
        }

        override suspend fun updateItem(item: TodoItem) {
            val index = items.indexOfFirst { it.id == item.id }
            if (index != -1) {
                items[index] = item
            }
        }
    }

    @Test
    fun `invoke adds item to repository and returns it`() = runTest {
        val repo = FakeTodoItemRepository()
        val useCase = AddItemUseCase(repo)

        val item = useCase("Test todo")

        assertEquals("Test todo", item.text)
        assertNotNull(item.id)
        assertEquals(false, item.isChecked)
        assertEquals(false, item.isFavorite)
        assertEquals(listOf(item), repo.items)
    }
}
