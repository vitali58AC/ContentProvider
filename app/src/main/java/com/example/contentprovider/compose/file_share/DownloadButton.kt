package com.example.contentprovider.compose.file_share

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contentprovider.R
import com.example.contentprovider.compose.RunLaunchedEffect
import com.example.contentprovider.file_share.FileShareViewModel
import com.example.contentprovider.ui.theme.lightGray600

@Composable
fun DownloadButton(text: String, onCLick: () -> Unit, enabled: Boolean = true) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                onCLick()
            },
            colors = ButtonDefaults.buttonColors(
                lightGray600
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(6.dp),
            enabled = enabled
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.W600
            )

        }
    }
}


@Composable
fun DownloadStatusToast(message: String, context: Context, viewModel: FileShareViewModel) {
    val toast = { m: String -> Toast.makeText(context, m, Toast.LENGTH_SHORT).show() }
    val unSuccessMessage = stringResource(id = R.string.un_success_message)
    val successMessage = stringResource(id = R.string.success_message, formatArgs = arrayOf(message))
    val errorMessage = stringResource(id = R.string.error_message, formatArgs = arrayOf(message))
    RunLaunchedEffect {
        when {
            message == "" -> toast(unSuccessMessage)
            !message.contains("HttpException") -> toast(successMessage)
            else -> toast(errorMessage)
        }
    }
    viewModel.setDownloadToFalse()
}