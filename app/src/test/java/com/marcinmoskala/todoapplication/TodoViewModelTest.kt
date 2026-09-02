package com.marcinmoskala.todoapplication

import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.domain.repositories.TodoItemRepository
import com.marcinmoskala.todoapplication.domain.usecase.AddItemUseCase
import com.marcinmoskala.todoapplication.ui.todos.TodoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeTodoItemRepository(
        initialItems: List<TodoItem> = emptyList()
    ) : TodoItemRepository {
        val items = initialItems.toMutableList()

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

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial items loaded from repository`() = runTest {
        val testItems = listOf(
            TodoItem("1", false, false, "Item 1"),
            TodoItem("2", true, false, "Item 2")
        )
        val repository = FakeTodoItemRepository(testItems)
        val useCase = AddItemUseCase(repository)
        val viewModel = TodoViewModel(repository, useCase)

        advanceUntilIdle()

        assertEquals(testItems, viewModel.items)
    }

    @Test
    fun `addItem adds new item and closes dialog`() = runTest {
        val repository = FakeTodoItemRepository()
        val useCase = AddItemUseCase(repository)
        val viewModel = TodoViewModel(repository, useCase)

        advanceUntilIdle()

        viewModel.onAddItemClicked()
        assertEquals("", viewModel.addDialog)

        viewModel.addItem("New task")
        advanceUntilIdle()

        assertNull(viewModel.addDialog)
        assertTrue(viewModel.items.any { it.text == "New task" })
    }

    @Test
    fun `onToggleCheckbox updates item state`() = runTest {
        val testItems = listOf(TodoItem("1", false, false, "Item 1"))
        val repository = FakeTodoItemRepository(testItems)
        val useCase = AddItemUseCase(repository)
        val viewModel = TodoViewModel(repository, useCase)

        advanceUntilIdle()

        viewModel.onToggleCheckbox("1", true)
        advanceUntilIdle()

        assertEquals(true, viewModel.items.first().isChecked)
    }

    @Test
    fun `onDeleteItem removes item`() = runTest {
        val testItems = listOf(TodoItem("1", false, false, "Item 1"))
        val repository = FakeTodoItemRepository(testItems)
        val useCase = AddItemUseCase(repository)
        val viewModel = TodoViewModel(repository, useCase)

        advanceUntilIdle()

        viewModel.onDeleteItem("1")
        advanceUntilIdle()

        assertTrue(viewModel.items.isEmpty())
    }
}
