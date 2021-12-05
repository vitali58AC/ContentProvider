package com.example.contentprovider.compose.contacts_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contentprovider.MainActivityViewModel
import com.example.contentprovider.R
import com.example.contentprovider.compose.GrayDivider
import com.example.contentprovider.compose.ProgressIndicator
import com.example.contentprovider.compose.TitleText
import com.example.contentprovider.data.Contact
import com.example.contentprovider.ui.theme.lightGray150
import com.example.contentprovider.ui.theme.lightGray600

@Composable
fun ContactsListScreen(
    viewModel: MainActivityViewModel,
    onPhoneClick: (Long) -> Unit,
    onFABClick: () -> Unit
) {
    val contacts = viewModel.contacts
    val isLoading = viewModel.isLoading.value
    Scaffold(
        floatingActionButton = { AddContactButton(onFABClick) }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(6.dp),
            modifier = Modifier
                .background(lightGray150)
                .fillMaxSize()
        ) {
            item { TitleText(stringResource(R.string.contacts), 45.sp) }
            items(
                items = contacts.sortedBy { it.name[0] },
                itemContent = {
                    ContactList(
                        item = it,
                        onPhoneClick
                    )
                }
            )
            item { Spacer(modifier = Modifier.height(70.dp)) }
        }
        ProgressIndicator(visibility = isLoading)
    }
}


@Composable
fun ContactList(
    item: Contact,
    onPhoneClick: (Long) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier
            .clickable { onPhoneClick(item.id) }) {
            ContactItem(contact = item)
            GrayDivider()
        }
    }
}

@Composable
fun AddContactButton(onFABClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onFABClick() },
        backgroundColor = lightGray600
    ) {
        Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_contact_button))
    }
}

