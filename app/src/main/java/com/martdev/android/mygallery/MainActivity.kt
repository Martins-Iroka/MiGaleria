package com.martdev.android.mygallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import com.martdev.ui.reusable.Navigator
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.compose.navigation3.getEntryProvider
import org.koin.androidx.scope.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.scope.Scope

@OptIn(KoinExperimentalAPI::class)
class MainActivity : ComponentActivity(), AndroidScopeComponent {
    override val scope: Scope by activityRetainedScope()
    val navigator: Navigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold {
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