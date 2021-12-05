package com.example.contentprovider.compose.detail_contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contentprovider.R
import com.example.contentprovider.compose.CircleFirstChar
import com.example.contentprovider.compose.GrayDivider
import com.example.contentprovider.data.Contact
import com.example.contentprovider.ui.theme.lightGray150
import com.example.contentprovider.ui.theme.lightGray600
import com.example.contentprovider.ui.theme.lightGray800

@Composable
fun DetailContactScreen(contact: Contact, context: Context, onClickDelete: () -> Unit) {
    Column(
        Modifier
            .background(lightGray150)
            .fillMaxSize()
            .padding(6.dp)
    ) {
        DetailHeader(contact.name)
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .defaultMinSize(0.dp, 200.dp)
                .padding(8.dp)
        ) {
            LittleTitle(text = stringResource(R.string.phone_numbers))
            for (i in contact.number) {
                val convertedPhone = i.replace("-", "")
                Text(
                    text = convertedPhone,
                    fontSize = 19.sp,
                    modifier = Modifier.clickable { callToPhone(convertedPhone, context) }
                )
                GrayDivider()
            }
            Spacer(modifier = Modifier.height(16.dp))
            LittleTitle(text = stringResource(R.string.email))
            for (i in contact.email) {
                Text(
                    text = i,
                    fontSize = 19.sp
                )
                GrayDivider()
            }
        }
        DeleteButton(onClickDelete)
    }
}

@Composable
fun DetailHeader(name: String) {
    Spacer(modifier = Modifier.height(50.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .defaultMinSize(0.dp, 150.dp)
            .padding(16.dp)
    ) {
        CircleFirstChar(name = name, size = 55.dp, fontSize = 20.sp)
        Text(
            text = name,
            modifier = Modifier.padding(20.dp),
            fontSize = 20.sp
        )
    }
}

@Composable
fun LittleTitle(text: String) {
    Text(text = text, color = lightGray600, fontSize = 10.sp)
}

@Composable
fun DeleteButton(onClickDelete: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
            contentDescription = stringResource(
                R.string.delete_contact
            ),
            modifier = Modifier
                .size(50.dp)
                .clickable { onClickDelete() },
            colorFilter = ColorFilter.tint(lightGray800)
        )
        Text(
            text = stringResource(id = R.string.delete_contact),
            modifier = Modifier.clickable { onClickDelete() })
    }
}

fun callToPhone(phone: String, context: Context) {
    Intent(Intent.ACTION_DIAL)
        .apply { data = Uri.parse("tel:$phone") }
        .also { context.startActivity(it) }
}