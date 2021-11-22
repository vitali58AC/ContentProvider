package com.example.contentprovider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.contentprovider.compose.contacts_list.ContactsListScreen
import com.example.contentprovider.compose.detail_contact.DetailContactScreen
import com.example.contentprovider.ui.theme.ContentProviderTheme

class MainActivity : ComponentActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentProviderTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                    val navController = rememberNavController()
                    NavigationComponent(
                        navController = navController,
                        viewModel = mainActivityViewModel
                    )
                }
            }
        }
    }
}


@Composable
fun NavigationComponent(navController: NavHostController, viewModel: MainActivityViewModel) {
    NavHost(
        navController = navController,
        startDestination = "contacts_list"
    ) {
        composable("contacts_list") {
            ContactsListScreen(navController = navController, viewModel = viewModel)
        }
        composable("detail_contact") {
            DetailContactScreen(viewModel = viewModel)
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ContentProviderTheme {
        Greeting("Android")
    }
}