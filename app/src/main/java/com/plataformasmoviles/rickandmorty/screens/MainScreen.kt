package com.plataformasmoviles.rickandmorty.screens

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.plataformasmoviles.rickandmorty.components.BottomDestination
import com.plataformasmoviles.rickandmorty.components.BottomNavigationBar
import com.plataformasmoviles.rickandmorty.navigation.CharactersDestination
import com.plataformasmoviles.rickandmorty.navigation.CharactersGraph
import com.plataformasmoviles.rickandmorty.navigation.LocationDetailDestination
import com.plataformasmoviles.rickandmorty.navigation.LocationsDestination
import com.plataformasmoviles.rickandmorty.navigation.LocationsGraph
import com.plataformasmoviles.rickandmorty.navigation.ProfileDestination
import com.plataformasmoviles.rickandmorty.navigation.charactersNavigation
import com.plataformasmoviles.rickandmorty.navigation.locationsNavigation
import com.plataformasmoviles.rickandmorty.ui.theme.RickAndMortyTheme
import androidx.navigation.NavGraph.Companion.findStartDestination

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {

    val navController = rememberNavController()

    val context = LocalContext.current
    val activity = context as? Activity

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = backStackEntry?.destination

    val selectedDestination = when {

        currentDestination?.route?.contains("Locations") == true -> {
            BottomDestination.LOCATIONS
        }

        currentDestination?.route?.contains("Profile") == true -> {
            BottomDestination.PROFILE
        }

        else -> {
            BottomDestination.CHARACTERS
        }
    }

    Scaffold(
        bottomBar = {

            BottomNavigationBar(
                selectedDestination = selectedDestination,

                onCharactersClick = {
                    navController.navigate(CharactersGraph) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                onLocationsClick = {
                    navController.navigate(LocationsGraph) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                onProfileClick = {
                    navController.navigate(ProfileDestination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(innerPadding)
        ) {

            charactersNavigation(
                navController = navController,
                onExitApp = {
                    activity?.finishAndRemoveTask()
                }
            )

            locationsNavigation(
                navController = navController
            )

            composable<ProfileDestination> {

                ProfileScreen(
                    onLogoutClick = onLogout
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun MainScreenPreview() {
    RickAndMortyTheme {
        MainScreen(
            onLogout = {}
        )
    }
}