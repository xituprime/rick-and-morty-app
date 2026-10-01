package com.plataformasmoviles.rickandmorty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.plataformasmoviles.rickandmorty.screens.CharacterDetailScreen
import com.plataformasmoviles.rickandmorty.screens.CharactersScreen

fun NavGraphBuilder.charactersNavigation(
    navController: NavHostController,
    onExitApp: () -> Unit
) {

    navigation<CharactersGraph>(
        startDestination = CharactersDestination
    ) {

        composable<CharactersDestination> {

            CharactersScreen(
                onCharacterClick = { characterId ->

                    navController.navigate(
                        CharacterDetailDestination(
                            id = characterId
                        )
                    )
                },
                onBack = onExitApp
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