package com.marcinmoskala.todoapplication.domain.usecase

import com.marcinmoskala.todoapplication.domain.data.TodoItem
import com.marcinmoskala.todoapplication.domain.repositories.TodoItemRepository
import java.util.UUID
import javax.inject.Inject

class AddItemUseCase @Inject constructor(
    private val itemRepository: TodoItemRepository
) {
    suspend operator fun invoke(text: String): TodoItem {
        val nextId = UUID.randomUUID().toString()
        val newItem = TodoItem(
            id = nextId,
            isChecked = false,
            isFavorite = false,
            text = text
        )
        itemRepository.addItem(newItem)
        return newItem
    }
}