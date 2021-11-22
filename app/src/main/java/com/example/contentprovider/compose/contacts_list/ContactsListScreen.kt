package com.example.contentprovider.compose.contacts_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.contentprovider.MainActivityViewModel

@Composable
fun ContactsListScreen(navController: NavHostController, viewModel: MainActivityViewModel) {
    val contacts = viewModel.contacts
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(
            items = contacts,
            itemContent = {
                Card(modifier = Modifier.clickable { }) {
                    ItemContact(contact = it) }
            }
        )

    }
}