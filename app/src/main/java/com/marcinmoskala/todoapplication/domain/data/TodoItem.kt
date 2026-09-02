package com.marcinmoskala.todoapplication.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val id: String,
    val isChecked: Boolean = false,
    val isFavorite: Boolean = false,
    val text: String,
)