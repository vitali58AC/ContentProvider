package com.example.contentprovider.compose.file_share

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.contentprovider.R
import com.example.contentprovider.file_share.CustomText
import com.example.contentprovider.file_share.FileShareViewModel

@Composable
fun FileShareScreen(viewModel: FileShareViewModel) {
    val currentText = viewModel.url.value
    val active = viewModel.active.value
    val isLoading = viewModel.isLoading.value
    val downloadStatus by viewModel.downloadStatus.observeAsState(false)
    val context = LocalContext.current
    Column {
        LogoImage()
        Spacer(modifier = Modifier.height(60.dp))
        CustomText(text = stringResource(R.string.enter_link_to_download))
        EditText(
            active = active,
            label = "URL",
            currentText = currentText
        ) { viewModel.updateUrl(it) }
        DownloadButton(
            text = stringResource(R.string.download),
            onCLick = {
                viewModel.getFileFromUrl(viewModel.url.value, context)
            },
            enabled = active
        )
        DownloadButton(
            text = "Share URL text",
            onCLick = { shareText(viewModel.url.value, context) })
        DownloadButton(
            text = "Save png from url to file",
            onCLick = {
                viewModel.getPngFromUrl(
                    context,
                    viewModel.url.value.split("/").last(),
                    viewModel.url.value
                )
            }
        )
        DownloadButton(
            text = "Share png",
            onCLick = { viewModel.sharePng(context) },
            enabled = viewModel.downloadPngStatus.value
        )
        if (downloadStatus) {
            DownloadStatusToast(viewModel.fileName.value, context, viewModel)
        }
        ProgressIndicator(visibility = isLoading)
    }
}

private fun shareText(text: String, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(intent, null)
    context.startActivity(shareIntent)
}
