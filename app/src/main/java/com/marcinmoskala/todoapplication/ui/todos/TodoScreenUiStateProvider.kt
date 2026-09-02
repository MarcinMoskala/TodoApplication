package com.marcinmoskala.todoapplication.ui.todos

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class TodoScreenUiStateProvider: PreviewParameterProvider<TodoScreenUiState> {
    private val data = mapOf(
        "1. Empty" to TodoScreenUiState(
            addDialog = null,
            items = emptyList()
        ),
        "2. Populated" to TodoScreenUiState(
            addDialog = null,
            items = Initial,
        ),
        "3. Overpopulated" to TodoScreenUiState(
            addDialog = null,
            items = (1..4).flatMapIndexed { index, _ ->
                Initial.map { it.copy(id = it.id + 100 * index) }
            },
        ),
        "4. Dialog" to TodoScreenUiState(
            addDialog = "ABC",
            items = Initial,
        ),
    )

    override fun getDisplayName(index: Int): String = data.toList().getOrNull(index)?.first ?: ""

    override val values: Sequence<TodoScreenUiState> = data.values.asSequence()
}