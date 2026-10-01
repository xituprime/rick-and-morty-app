package com.plataformasmoviles.rickandmorty.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plataformasmoviles.rickandmorty.screens.LoginScreen
import com.plataformasmoviles.rickandmorty.screens.MainScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {

        composable<LoginDestination> {

            LoginScreen(
                onStartClick = {

                    navController.navigate(MainDestination) {

                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<MainDestination> {

            MainScreen(
                onLogout = {

                    navController.navigate(LoginDestination) {

                        popUpTo<MainDestination> {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }
    }
}