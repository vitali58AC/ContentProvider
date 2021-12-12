package com.example.contentprovider.compose

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.example.contentprovider.compose.costume_provider.CourseDetailScreen
import com.example.contentprovider.compose.costume_provider.CustomContentProviderScreen
import com.example.contentprovider.compose.costume_provider.UserDetailScreen
import com.example.contentprovider.compose.detail_contact.DetailContactScreen
import com.example.contentprovider.costume_content_provider.CustomContentViewModel
import com.example.contentprovider.utils.Constants

@Composable
fun NavigationComponent(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    context: Context,
    customContentViewModel: CustomContentViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Constants.CONTACT_LIST
    ) {
        composable(Constants.CONTACT_LIST) {
            ContactsListScreen(
                viewModel = viewModel,
                onPhoneClick = { phone ->
                    navigateToSingleAccount(
                        navController, phone,
                        Constants.DETAIL_CONTACT
                    )
                },
                onFABClick = { navController.navigate(Constants.ADD_CONTACT) },
                onTopAppBarClick = { navController.navigate((Constants.COSTUME_PROVIDER)) }
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
                    navigateToHomeScreenWithoutBackStack(navController)
                }
            }
        }
        composable(Constants.ADD_CONTACT) {
            AddContactScreen(
                onCancelClick = { navigateToHomeScreenWithoutBackStack(navController) },
                onSaveClick = { name, phone, email ->
                    viewModel.saveContact(name, phone, email)
                    navigateToHomeScreenWithoutBackStack(navController)
                }
            )
        }
        composable(Constants.COSTUME_PROVIDER) {
            CustomContentProviderScreen(
                onUserClick = { id ->
                    navigateToSingleAccount(
                        navController,
                        id,
                        Constants.USER_DETAIL
                    )
                },
                onCourseClick = { id ->
                    navigateToSingleAccount(
                        navController,
                        id,
                        Constants.COURSE_DETAIL
                    )
                },
                viewModel = customContentViewModel
            )
        }
        composable(
            route = "${Constants.USER_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val userId = entry.arguments?.getLong("id")
            val user = customContentViewModel.getUser(userId)
            UserDetailScreen(user = user)
        }
        composable(
            route = "${Constants.COURSE_DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { entry ->
            val courseId = entry.arguments?.getLong("id")
            val course = customContentViewModel.getCourse(courseId)
            CourseDetailScreen(course = course)
        }
    }
}

fun navigateToHomeScreenWithoutBackStack(navController: NavHostController) {
    navController.navigate(Constants.CONTACT_LIST) {
        popUpTo(Constants.CONTACT_LIST) { inclusive = true }
    }
}

private fun navigateToSingleAccount(
    navController: NavHostController,
    id: Long,
    route: String
) {
    navController.navigate("$route/$id")
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
