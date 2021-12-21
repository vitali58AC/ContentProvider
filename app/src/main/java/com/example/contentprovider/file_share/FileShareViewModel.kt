package com.example.contentprovider.file_share

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contentprovider.BuildConfig
import kotlinx.coroutines.*
import java.io.File

class FileShareViewModel : ViewModel() {

    private val repository = FileShareRepository()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    var url = mutableStateOf("")
    var fileName = mutableStateOf("")
    var active = mutableStateOf(true)
    var isLoading = mutableStateOf(false)
    private var sharePngFileName = mutableStateOf("")
    var downloadPngStatus = mutableStateOf(false)



    private val _downloadStatus = MutableLiveData<Boolean>()
    val downloadStatus: LiveData<Boolean>
        get() = _downloadStatus

    fun updateUrl(newUrl: String) {
        url.value = newUrl
    }

    fun setDownloadToFalse() {
        _downloadStatus.value = false
    }

    fun getFileFromUrl(url: String, context: Context) {
        viewModelScope.launch {
            isLoading.value = true
            active.value = false
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@launch
            fileName.value = repository.getFileFromUrl(url, context)
            active.value = true
            isLoading.value = false
            _downloadStatus.postValue(true)
        }
    }

    fun getPngFromUrl(context: Context, filename: String, url: String) {
        scope.launch {
            sharePngFileName.value = filename
            repository.downloadAnImage(context = context, fileName = filename, url = url)
            downloadPngStatus.value = true
        }
    }

    fun sharePng(context: Context) {
        scope.launch {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) return@launch
            val dir = context.getExternalFilesDir("saved_files")
            val image = File(dir, sharePngFileName.value)
            if (image.exists().not()) return@launch
            val uri = FileProvider.getUriForFile(
                context,
                "${BuildConfig.APPLICATION_ID}.file_provider",
                image
            )
            Log.e("file_share_viewModel", uri.toString())
            val intent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, uri)
                type = context.contentResolver.getType(uri)
            }
            val shareIntent = Intent.createChooser(intent, null)
            context.startActivity(shareIntent)
        }
    }

    override fun onCleared() {
        scope.cancel()
        super.onCleared()
    }
}