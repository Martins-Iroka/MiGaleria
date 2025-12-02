package com.martdev.ui.reusable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

sealed class NavigateTo {
    data object Login : NavigateTo()
    data object Registration : NavigateTo()
    data class Verification(val email: String) : NavigateTo()
}

class AppNavigator(startDestination: Any) {

    private var topLevelStacks : LinkedHashMap<Any, SnapshotStateList<Any>> = linkedMapOf(
        startDestination to mutableStateListOf(startDestination)
    )

    var topLevelDestination by mutableStateOf(startDestination)
        private set

    val backStack = mutableStateListOf(startDestination)

    private fun updateBackStack() = backStack.apply {
        clear()
        addAll(topLevelStacks.flatMap { it.value })
    }

    fun addTopLevel(destination: Any) {
        if (topLevelStacks[destination] == null) {
            topLevelStacks[destination] = mutableStateListOf(destination)
        } else {
            topLevelStacks.apply {
                remove(destination)?.let {
                    put(destination, it)
                }
            }
        }
        topLevelDestination = destination
        updateBackStack()
    }

    fun goTo(destination: Any) {
        topLevelStacks[topLevelDestination]?.add(destination)
        updateBackStack()
    }

    fun goBack() {
        val removedKey = topLevelStacks[topLevelDestination]?.removeLastOrNull()
        topLevelStacks.remove(removedKey)
        topLevelDestination = topLevelStacks.keys.last()
        updateBackStack()
    }

    fun popUpTo(destination: Any) {
        val currentStack = topLevelStacks[topLevelDestination] ?: return
        val lastIndexOfT = currentStack.lastIndexOf(destination)

        // Do nothing if the destination isn't in the current top-level stack
        if (lastIndexOfT == -1) {
            println("Warning: popUpTo destination not found in current top-level stack.")
            return
        }

        val targetIndex = lastIndexOfT + 1

        // Ensure we don't try to remove items out of bounds
        if (targetIndex >= currentStack.size) return

        // Remove the items from the specific top-level stack
        currentStack.removeRange(targetIndex, currentStack.size)

        // Sync the main backStack to reflect the changes
        updateBackStack()
    }
}