package com.example.contentprovider.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import com.example.contentprovider.ui.theme.lightGray200

@Composable
fun CircleFirstChar(name: String, size: Dp, fontSize: TextUnit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
            .background(lightGray200)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = name.first().toString(), fontSize = fontSize)
        }
    }
}
