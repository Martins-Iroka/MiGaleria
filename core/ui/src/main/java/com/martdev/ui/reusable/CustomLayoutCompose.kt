package com.martdev.ui.reusable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomLayout(
    message: String= "",
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState= SnackbarHostState(),
    bottomView: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = bottomView,
        snackbarHost = {
        SnackbarHost(hostState = snackBarHostState)
    }) {
        content(it)
    }
}