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
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.plataformasmoviles.rickandmorty.data.local.AppDatabase
import com.plataformasmoviles.rickandmorty.data.local.toModel
import com.plataformasmoviles.rickandmorty.screens.CharacterFormScreen
import kotlinx.coroutines.flow.first
import androidx.navigation.toRoute

fun NavGraphBuilder.charactersNavigation(
    navController: NavHostController,
    onExitApp: () -> Unit,

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
                onCreateClick = {
                    navController.navigate(CreateCharacterDestination)
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
                },

                onEditClick = {
                    uiState.data?.let { character ->
                        navController.navigate(
                            EditCharacterDestination(character.id)
                        )
                    }
                },

                onDeleteClick = {
                    uiState.data?.let { character ->
                        val listEntry = navController.getBackStackEntry(
                            CharactersDestination
                        )

                        val charactersViewModel: CharactersViewModel =
                            androidx.lifecycle.ViewModelProvider(
                                listEntry
                            )[CharactersViewModel::class.java]

                        charactersViewModel.deleteCharacter(character.id) {
                            navController.popBackStack()
                        }
                    }
                },
            )
        }

        composable<CreateCharacterDestination> {

            val charactersViewModel: CharactersViewModel = viewModel()

            CharacterFormScreen(
                onSave = { name, species, status, image, gender ->

                    charactersViewModel.createCharacter(
                        name = name,
                        species = species,
                        status = status,
                        image = image,
                        gender = gender,
                        onSuccess = {
                            navController.popBackStack()
                        }
                    )
                },

                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable<EditCharacterDestination> { backStackEntry ->

            val route = backStackEntry
                .toRoute<EditCharacterDestination>()

            val context = LocalContext.current.applicationContext

            val dao = remember(context) {
                AppDatabase.getDatabase(context).characterDao()
            }

            val character by produceState<
                    com.plataformasmoviles.rickandmorty.model.Character?
                    >(
                initialValue = null,
                key1 = route.id
            ) {
                value = dao.getCharacterById(route.id)
                    .first()
                    ?.toModel()
            }

            val charactersViewModel: CharactersViewModel = viewModel()

            character?.let { currentCharacter ->

                CharacterFormScreen(
                    character = currentCharacter,

                    onSave = { name, species, status, image, gender ->

                        charactersViewModel.updateCharacter(
                            id = route.id,
                            name = name,
                            species = species,
                            status = status,
                            image = image,
                            gender = gender,
                            onSuccess = {
                                navController.popBackStack()
                            }
                        )
                    },

                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }
        }

    }
}