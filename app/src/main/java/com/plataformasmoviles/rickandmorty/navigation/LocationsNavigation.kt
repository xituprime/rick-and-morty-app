package com.plataformasmoviles.rickandmorty.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.plataformasmoviles.rickandmorty.screens.LocationDetailScreen
import com.plataformasmoviles.rickandmorty.screens.LocationsScreen
import com.plataformasmoviles.rickandmorty.viewmodel.LocationDetailViewModel
import com.plataformasmoviles.rickandmorty.viewmodel.LocationsViewModel

fun NavGraphBuilder.locationsNavigation(
    navController: NavHostController
) {
    navigation<LocationsGraph>(
        startDestination = LocationsDestination
    ) {

        composable<LocationsDestination> {

            val locationsViewModel: LocationsViewModel = viewModel()

            val uiState by locationsViewModel.uiState
                .collectAsStateWithLifecycle()

            LocationsScreen(
                uiState = uiState,

                onLocationClick = { locationId ->
                    navController.navigate(
                        LocationDetailDestination(id = locationId)
                    )
                },

                onRetry = {
                    locationsViewModel.loadLocations()
                },

                onLoadingClick = {
                    locationsViewModel.simulateError()
                }
            )
        }

        composable<LocationDetailDestination> {

            val detailViewModel: LocationDetailViewModel = viewModel()

            val uiState by detailViewModel.uiState
                .collectAsStateWithLifecycle()

            LocationDetailScreen(
                uiState = uiState,

                onBackClick = {
                    navController.popBackStack()
                },

                onRetry = {
                    detailViewModel.loadLocation()
                },

                onLoadingClick = {
                    detailViewModel.simulateError()
                }
            )
        }
    }
}