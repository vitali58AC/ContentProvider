package com.example.contentprovider.compose

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.contentprovider.MainActivityViewModel
import com.example.contentprovider.R
import com.example.contentprovider.compose.add_contact.AddContactScreen
import com.example.contentprovider.compose.contacts_list.ContactsListScreen
import com.example.contentprovider.compose.detail_contact.DetailContactScreen
import com.example.contentprovider.utils.Constants

@Composable
fun NavigationComponent(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = Constants.CONTACT_LIST
    ) {
        composable(Constants.CONTACT_LIST) {
            ContactsListScreen(
                viewModel = viewModel,
                onPhoneClick = { phone -> navigateToSingleAccount(navController, phone) },
                onFABClick = { navController.navigate(Constants.ADD_CONTACT) }
            )
        }
        composable(
            route = "${Constants.DETAIL_CONTACT}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val profileName = entry.arguments?.getLong("id")
            val profile = viewModel.getProfile(profileName)
            DetailContactScreen(profile, context) {
                showDeleteDialog(
                    R.string.delete_title,
                    R.string.delete_message,
                    context
                ) {
                    viewModel.deleteContact(profile.id)
                    viewModel.deleteViewModelListOfContacts(profile.id)
                    navController.navigate(Constants.CONTACT_LIST) {
                        popUpTo(Constants.CONTACT_LIST) { inclusive = true }
                    }
                }
            }
        }
        composable(Constants.ADD_CONTACT) {
            AddContactScreen()
        }
    }
}

private fun navigateToSingleAccount(
    navController: NavHostController,
    id: Long
) {
    navController.navigate("${Constants.DETAIL_CONTACT}/$id")
}

private fun showDeleteDialog(
    @StringRes titleResId: Int,
    @StringRes messageResId: Int,
    context: Context,
    onDelete: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(titleResId)
        .setPositiveButton("Yes") { _, _ -> onDelete() }
        .setNegativeButton("No") { _, _ -> }
        .setMessage(messageResId)
        .show()
}
