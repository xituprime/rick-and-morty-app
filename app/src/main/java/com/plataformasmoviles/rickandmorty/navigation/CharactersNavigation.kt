package com.plataformasmoviles.rickandmorty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.plataformasmoviles.rickandmorty.screens.CharacterDetailScreen
import com.plataformasmoviles.rickandmorty.screens.CharactersScreen
import com.plataformasmoviles.rickandmorty.viewmodel.CharacterDetailViewModel
import com.plataformasmoviles.rickandmorty.viewmodel.CharactersViewModel

fun NavGraphBuilder.charactersNavigation(
    navController: NavHostController,
    onExitApp: () -> Unit
) {
    navigation<CharactersGraph>(
        startDestination = CharactersDestination
    ) {

        composable<CharactersDestination> {

            val charactersViewModel: CharactersViewModel = viewModel()

            val uiState by charactersViewModel.uiState
                .collectAsStateWithLifecycle()

            CharactersScreen(
                uiState = uiState,

                onCharacterClick = { characterId ->
                    navController.navigate(
                        CharacterDetailDestination(id = characterId)
                    )
                },

                onRetry = {
                    charactersViewModel.loadCharacters()
                },

                onLoadingClick = {
                    charactersViewModel.simulateError()
                },

                onFavoriteClick = { characterId ->
                    charactersViewModel.toggleFavorite(characterId)
                },

                onFilterClick = {
                    charactersViewModel.toggleFavoritesFilter()
                },

                onBack = onExitApp
            )
        }

        composable<CharacterDetailDestination> {

            val detailViewModel: CharacterDetailViewModel = viewModel()

            val uiState by detailViewModel.uiState
                .collectAsStateWithLifecycle()

            CharacterDetailScreen(
                uiState = uiState,

                onBackClick = {
                    navController.popBackStack()
                },

                onRetry = {
                    detailViewModel.loadCharacter()
                },

                onLoadingClick = {
                    detailViewModel.simulateError()
                },

                onFavoriteClick = {
                    detailViewModel.toggleFavorite()
                }
            )
        }
    }
}