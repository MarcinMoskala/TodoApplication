package com.marcinmoskala.todoapplication.ui.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.marcinmoskala.todoapplication.ui.navigation.Destination
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    var navBackStack by mutableStateOf(listOf<Destination>(Destination.Todos))

    fun navigateTo(destination: Destination) {
        navBackStack += destination
    }

    fun navigateBack() {
        navBackStack = navBackStack.dropLast(1)
    }
}