package com.example.contentprovider

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.contentprovider.data.Contact

class MainActivityViewModel: ViewModel() {

    val contacts = mutableStateListOf<Contact>()
}