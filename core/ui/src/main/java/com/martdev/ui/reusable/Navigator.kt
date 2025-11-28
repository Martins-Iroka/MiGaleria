package com.martdev.ui.reusable

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

sealed class NavigateTo {
    data object Login : NavigateTo()
    data object Registration : NavigateTo()
    data class Verification(val email: String) : NavigateTo()
}
class Navigator(startDestination: Any) {
    val backStack : SnapshotStateList<Any> = mutableStateListOf(startDestination)

    fun goTo(destination: Any){
        backStack.add(destination)
    }

    fun goBack(){
        backStack.removeLastOrNull()
    }

    inline fun <reified T : Any> popUpTo(inclusive: Boolean = false) {
        val lastIndexOfT = backStack.indexOfLast { it is T }
        if (lastIndexOfT == -1) return

        val targetIndex = if (inclusive) lastIndexOfT else lastIndexOfT + 1

        backStack.removeRange(targetIndex, backStack.size)
    }
}