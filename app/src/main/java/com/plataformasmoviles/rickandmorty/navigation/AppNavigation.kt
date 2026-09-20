package com.plataformasmoviles.rickandmorty.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.plataformasmoviles.rickandmorty.screens.CharacterDetailScreen
import com.plataformasmoviles.rickandmorty.screens.CharactersScreen
import com.plataformasmoviles.rickandmorty.screens.LoginScreen
import android.app.Activity
import androidx.compose.ui.platform.LocalContext

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val context = LocalContext.current
    val activity = context as? Activity

    NavHost(
        navController = navController,
        startDestination = LoginDestination
    ) {

        composable<LoginDestination> {

            LoginScreen(
                onStartClick = {

                    navController.navigate(CharactersDestination) {

                        popUpTo<LoginDestination> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<CharactersDestination> {

            CharactersScreen(
                onCharacterClick = { characterId ->

                    navController.navigate(
                        CharacterDetailDestination(
                            id = characterId
                        )
                    )
                },
                onBack = {
                    activity?.finishAndRemoveTask()
                }
            )
        }

        composable<CharacterDetailDestination> { backStackEntry ->

            val destination =
                backStackEntry.toRoute<CharacterDetailDestination>()

            CharacterDetailScreen(
                characterId = destination.id,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}