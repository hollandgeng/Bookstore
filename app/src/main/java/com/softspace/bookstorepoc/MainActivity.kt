@file:OptIn(ExperimentalMaterial3Api::class)

package com.softspace.bookstorepoc

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.softspace.bookstorepoc.ui.theme.BookstoreTheme
import androidx.navigation.compose.rememberNavController
import com.softspace.bookstorepoc.base.NavigationIntent
import com.softspace.bookstorepoc.viewmodels.BooklistViewModel
import com.softspace.bookstorepoc.viewmodels.LoginViewModel
import com.softspace.bookstorepoc.viewmodels.MainViewModel
import com.softspace.bookstorepoc.views.BookInfoScreen
import com.softspace.bookstorepoc.views.BooklistScreen
import com.softspace.bookstorepoc.views.LoginScreen
import dagger.hilt.android.AndroidEntryPoint
import helper.CustomNavHost
import helper.CustomNavigator
import helper.Screen
import helper.composable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window,false)

        setContent {
            BookstoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel())
{
    val navController = rememberNavController()

    NavigationEffects(navigationChannel = viewModel.navigationChannel, navHostController = navController)

    BookstoreTheme {
        CustomNavHost(
            navController = navController,
            startDestination = Screen.LoginScreen){
            composable(Screen.LoginScreen)
            {
                LoginScreen()
            }
            composable(Screen.BooklistScreen)
            {
                BooklistScreen()
            }
            composable(Screen.BookInfoScreen)
            {
                BookInfoScreen()
            }
        }

//        Scaffold (
//            topBar = {
//                CenterAlignedTopAppBar(
//                    colors =  TopAppBarDefaults.centerAlignedTopAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer
//                    ),
//                    title = { Text("SS Bookstore", color = Color.Black)},
//                )},
//            content = { paddingValues ->
//                NavHost(
//                    modifier = Modifier.padding(paddingValues),
//                    navController = navController,
//                    startDestination = BookstoreViews.Login.name){
//                    composable(BookstoreViews.Login.name)
//                    {
//                        LoginView {
//                            navController.navigate(BookstoreViews.Home.name)
//                        }
//                    }
//                    composable(BookstoreViews.Home.name)
//                    {
//                        Booklist();
//                    }
//                }
//            }
//        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppPreview()
{
    MainScreen()
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
)
{
    val activity = LocalContext.current as? Activity

    LaunchedEffect(activity, navHostController, navigationChannel)
    {
        navigationChannel.receiveAsFlow().collect{intent ->
            if(activity?.isFinishing == true)
            {
                return@collect
            }

            when(intent)
            {
                is NavigationIntent.NavigateBack -> {
                    if(intent.route != null)
                    {
                        navHostController.popBackStack(intent.route,true)
                    }
                    else
                    {
                        navHostController.popBackStack()
                    }
                }

                is  NavigationIntent.Navigate -> {
                    navHostController.navigate(intent.route)
                }

                else -> {}
            }
        }


    }
}
