package com.martdev.android.mygallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.martdev.LoginScreen
import com.martdev.ui.theme.MyGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyGalleryTheme {
                LoginScreen()
            }
        }
    }
}