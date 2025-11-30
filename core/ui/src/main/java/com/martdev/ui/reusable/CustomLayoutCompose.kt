package com.martdev.ui.reusable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun CustomLayout(
    message: String= "",
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {

    val snackBackHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackBackHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
        SnackbarHost(hostState = snackBackHostState)
    }) {
        content(it)
    }
}