package com.example.contentprovider

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController
import com.example.contentprovider.compose.NavigationComponent
import com.example.contentprovider.costume_content_provider.CustomContentViewModel
import com.example.contentprovider.ui.theme.ContentProviderTheme
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest

@ExperimentalFoundationApi
class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private val customContentViewModel: CustomContentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactsPermissions()
        customContentViewModel.saveDefaultTables()
        setContent {
            ContentProviderTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavigationComponent(
                        navController = navController,
                        viewModel = mainActivityViewModel,
                        context = this,
                        customContentViewModel = customContentViewModel
                    )
                }
            }
        }
    }

    private fun contactsPermissions() {
        //Post нужен, что бы поставить в очередь, пока обрабатывается менеджер фрагментов?
        Handler(Looper.getMainLooper()).post {
            constructPermissionsRequest(
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS,
                onShowRationale = ::showRationaleForContacts,
                onPermissionDenied = ::onContactsDenied,
                onNeverAskAgain = ::onContactsNeverAskAgain,
                requiresPermission = { mainActivityViewModel.loadContacts() }
            ).launch()
        }
    }


    private fun onContactsDenied() {
        Toast.makeText(this, getString(R.string.permissions_denied), Toast.LENGTH_SHORT).show()
    }

    private fun showRationaleForContacts(request: PermissionRequest) {
        showRationaleDialog(R.string.title_permissions, R.string.why_permissions, request)
    }

    private fun onContactsNeverAskAgain() {
        Toast.makeText(this, getString(R.string.never_ask_again), Toast.LENGTH_LONG).show()
    }

    private fun showRationaleDialog(
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        request: PermissionRequest
    ) {
        AlertDialog.Builder(this)
            .setTitle(titleResId)
            .setPositiveButton("Allow") { _, _ -> request.proceed() }
            .setNegativeButton("Denied") { _, _ ->
                request.cancel()
                onContactsDenied()
            }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }


}