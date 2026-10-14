
package com.plataformasmoviles.rickandmorty.navigation

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plataformasmoviles.rickandmorty.data.UserPreferencesRepository
import com.plataformasmoviles.rickandmorty.screens.LoginScreen
import com.plataformasmoviles.rickandmorty.screens.MainScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {

    val context = LocalContext.current.applicationContext

    val repository = remember(context) {
        UserPreferencesRepository(context)
    }

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    var initialUserName by remember {
        mutableStateOf<String?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(repository) {
        initialUserName = repository.userName.first()
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {

        NavHost(
            navController = navController,
            startDestination = if (initialUserName.isNullOrBlank()) {
                LoginDestination
            } else {
                MainDestination
            }
        ) {

            composable<LoginDestination> {

                LoginScreen(
                    onStartClick = { name ->

                        scope.launch {
                            repository.saveUserName(name)
                            initialUserName = name

                            navController.navigate(MainDestination) {
                                popUpTo<LoginDestination> {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable<MainDestination> {

                MainScreen(
                    userName = initialUserName.orEmpty(),
                    onLogout = {

                        scope.launch {
                            repository.clearUserName()
                            initialUserName =  null

                            navController.navigate(LoginDestination) {
                                popUpTo<MainDestination> {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}
