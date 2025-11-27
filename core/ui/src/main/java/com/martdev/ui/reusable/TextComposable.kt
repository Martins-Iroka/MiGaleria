package com.martdev.ui.reusable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.martdev.ui.reusable.theme.Color_4E0189

@Composable
fun TextCompose(
    text: String,
    modifier: Modifier = Modifier,
    font: Int = MANROP_SEMI_BOLD,
    fontSize: Int = 14,
    textColor: Color = Color_4E0189
) = Text(text = text,
    modifier = modifier,
    fontFamily = setFontFamily(font),
    color = textColor,
    fontSize = fontSize.sp)