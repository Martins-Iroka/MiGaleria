package com.martdev.android.mygallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import com.martdev.ui.reusable.AppNavigator
import com.martdev.ui.reusable.NavigateTo
import com.martdev.ui.reusable.TextCompose
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.compose.navigation3.getEntryProvider
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.scope.Scope

//work on the app navigation
@OptIn(KoinExperimentalAPI::class)
class MainActivity : ComponentActivity(), AndroidScopeComponent {
    override val scope: Scope by activityRetainedScope()
    val navigator: AppNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var selectedItem by remember {
                mutableIntStateOf(0)
            }
            val items = listOf(NavigateTo.Photo, NavigateTo.Video)
            val selectedIcons = listOf(Icons.Filled.PhotoLibrary, Icons.Filled.VideoLibrary)
            val unselectedIcons = listOf(Icons.Outlined.PhotoLibrary, Icons.Outlined.VideoLibrary)
            Scaffold(
                bottomBar = {
                    if (navigator.currentDestination is NavigateTo.Photo || navigator.currentDestination is NavigateTo.Video) {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                            contentDescription = item.toString()
                                        )
                                    },
                                    label = { TextCompose(item.toString()) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        navigator.addTopLevel(item)
                                    }
                                )
                            }
                        }
                    }
                }
            ) {
                NavDisplay(
                    backStack = navigator.backStack,
                    modifier = Modifier.padding(it),
                    onBack = {
                        navigator.goBack()
                             },
                    entryProvider = getEntryProvider()
                )
            }
        }
    }
}