package com.example.contentprovider.compose.contacts_list

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contentprovider.compose.CircleFirstChar
import com.example.contentprovider.data.Contact

@Composable
fun ContactItem(contact: Contact) {
    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
        CircleFirstChar(name = contact.name, size = 40.dp, fontSize = 17.sp)
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = contact.name,
            fontSize = 18.sp
        )
    }
}


