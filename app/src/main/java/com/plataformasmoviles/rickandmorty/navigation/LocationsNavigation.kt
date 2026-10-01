package com.plataformasmoviles.rickandmorty.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.plataformasmoviles.rickandmorty.screens.LocationDetailScreen
import com.plataformasmoviles.rickandmorty.screens.LocationsScreen

fun NavGraphBuilder.locationsNavigation(
    navController: NavHostController
) {

    navigation<LocationsGraph>(
        startDestination = LocationsDestination
    ) {

        composable<LocationsDestination> {

            LocationsScreen(
                onLocationClick = { locationId ->

                    navController.navigate(
                        LocationDetailDestination(
                            id = locationId
                        )
                    )
                }
            )
        }

        composable<LocationDetailDestination> { backStackEntry ->

            val destination =
                backStackEntry.toRoute<LocationDetailDestination>()

            LocationDetailScreen(
                locationId = destination.id,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}