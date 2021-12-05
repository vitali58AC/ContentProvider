package com.example.contentprovider.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.example.contentprovider.data.Contact

@Composable
fun RunLaunchedEffect(func: () -> Contact) {
    val currentCall by rememberUpdatedState(func)

    LaunchedEffect(true) {
        currentCall()
    }
}