package com.example.contentprovider.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contentprovider.ui.theme.lightGray200

@Composable
fun GrayDivider() {
    Divider(color = lightGray200, modifier = Modifier.padding(10.dp, 0.dp))
}