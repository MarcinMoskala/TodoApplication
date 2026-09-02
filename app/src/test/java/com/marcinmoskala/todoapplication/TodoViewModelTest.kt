package com.marcinmoskala.todoapplication

import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.domain.repositories.TodoItemRepository
import com.marcinmoskala.todoapplication.domain.usecase.AddItemUseCase
import com.marcinmoskala.todoapplication.ui.details.Navigator
import com.marcinmoskala.todoapplication.ui.navigation.Destination
import com.marcinmoskala.todoapplication.ui.todos.TodoUiAction
import com.marcinmoskala.todoapplication.ui.todos.TodoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    private val navigator = Navigator()

    private class FakeTodoItemRepository(
        initialItems: List<TodoItem> = emptyList()
    ) : TodoItemRepository {
        private val _itemsFlow = MutableStateFlow(initialItems)

        override fun observeTodoItems(): Flow<List<TodoItem>> = _itemsFlow.asStateFlow()

        override suspend fun getTodoItems(): List<TodoItem> = _itemsFlow.value

        override suspend fun addItem(newItem: TodoItem) {
            _itemsFlow.update { it + newItem }
        }

        override suspend fun removeItem(id: String) {
            _itemsFlow.update { items -> items.filterNot { it.id == id } }
        }

        override suspend fun updateItem(item: TodoItem) {
            _itemsFlow.update { items -> items.map { if (it.id == item.id) item else it } }
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
        val viewModel = TodoViewModel(repository, useCase, navigator)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

        advanceUntilIdle()

        assertEquals(testItems, viewModel.uiState.value.items)
    }

    @Test
    fun `addItem adds new item and closes dialog`() = runTest {
        val repository = FakeTodoItemRepository()
        val useCase = AddItemUseCase(repository)
        val viewModel = TodoViewModel(repository, useCase, navigator)

        advanceUntilIdle()

        viewModel.onAction(TodoUiAction.AddItemClicked)
        advanceUntilIdle()
        assertEquals("", viewModel.uiState.value.addDialog)

        viewModel.onAction(TodoUiAction.AddItem("New task"))
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.addDialog)
        assertTrue(viewModel.uiState.value.items.any { it.text == "New task" })
    }

    @Test
    fun `onToggleCheckbox updates item state`() = runTest {
        val testItems = listOf(TodoItem("1", false, false, "Item 1"))
        val repository = FakeTodoItemRepository(testItems)
        val useCase = AddItemUseCase(repository)
        val viewModel = TodoViewModel(repository, useCase, navigator)

        advanceUntilIdle()

        viewModel.onAction(TodoUiAction.ToggleCheckbox("1", true))
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.items.first().isChecked)
    }

    @Test
    fun `onDeleteItem removes item`() = runTest {
        val testItems = listOf(TodoItem("1", false, false, "Item 1"))
        val repository = FakeTodoItemRepository(testItems)
        val useCase = AddItemUseCase(repository)
        val viewModel = TodoViewModel(repository, useCase, navigator)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

        advanceUntilIdle()

        viewModel.onAction(TodoUiAction.DeleteItem("1"))
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.items.isEmpty())
    }

    @Test
    fun `onToggleFavorite updates favorite status`() = runTest {
        val testItems = listOf(TodoItem("1", false, false, "Item 1"))
        val repository = FakeTodoItemRepository(testItems)
        val useCase = AddItemUseCase(repository)
        val viewModel = TodoViewModel(repository, useCase, navigator)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect() }

        advanceUntilIdle()

        viewModel.onAction(TodoUiAction.ToggleFavorite("1"))
        advanceUntilIdle()

        assertEquals(true, viewModel.uiState.value.items.first().isFavorite)
    }

    @Test
    fun `onItemClicked navigates to TodoDetails`() = runTest {
        val repository = FakeTodoItemRepository()
        val useCase = AddItemUseCase(repository)
        val testNavigator = Navigator()
        val viewModel = TodoViewModel(repository, useCase, testNavigator)

        val item = TodoItem("1", false, false, "Item 1")
        viewModel.onAction(TodoUiAction.ItemClicked(item))

        assertEquals(listOf(Destination.Todos, Destination.TodoDetails(item)), testNavigator.navBackStack)
    }
}
