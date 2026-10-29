package com.plataformasmoviles.rickandmorty.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.plataformasmoviles.rickandmorty.data.UserPreferencesRepository
import com.plataformasmoviles.rickandmorty.data.local.AppDatabase
import com.plataformasmoviles.rickandmorty.data.local.InitialDataSync
import com.plataformasmoviles.rickandmorty.screens.LoginScreen
import com.plataformasmoviles.rickandmorty.screens.MainScreen
import com.plataformasmoviles.rickandmorty.screens.SyncScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {

    val context = LocalContext.current.applicationContext

    val repository = remember(context) {
        UserPreferencesRepository(context)
    }

    val database = remember(context) {
        AppDatabase.getDatabase(context)
    }

    val initialDataSync = remember(database) {
        InitialDataSync(database)
    }

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    var userName by remember {
        mutableStateOf<String?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var isLoggingOut by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(repository) {
        userName = repository.userName.first()
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
            startDestination = if (userName.isNullOrBlank()) {
                LoginDestination
            } else {
                SyncDestination
            }
        ) {

            // Pantalla de inicio de sesion
            composable<LoginDestination> {

                LoginScreen(
                    onStartClick = { name ->

                        scope.launch {
                            repository.saveUserName(name)
                            userName = name

                            navController.navigate(SyncDestination) {
                                popUpTo<LoginDestination> {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            // Pantalla de sincronizacion
            composable<SyncDestination> {

                LaunchedEffect(Unit) {

                    initialDataSync.synchronize()

                    navController.navigate(MainDestination) {
                        popUpTo<SyncDestination> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                SyncScreen()
            }

            // Pantalla principal
            composable<MainDestination> {

                MainScreen(
                    userName = userName.orEmpty(),


                    onLogout = {
                        if (!isLoggingOut) {
                            isLoggingOut = true

                            scope.launch {
                                try {
                                    // Limpiar primero los datos de Room
                                    database.characterDao().deleteAllCharacters()
                                    database.locationDao().deleteAllLocations()

                                    // Eliminar el nombre de DataStore
                                    repository.clearUserName()

                                    // Actualizar el estado
                                    userName = null

                                    // Regresar al Login y limpiar el historial
                                    navController.navigate(LoginDestination) {
                                        popUpTo(navController.graph.id) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }

                                } catch (e: Exception) {
                                    android.util.Log.e(
                                        "LOGOUT_ERROR",
                                        "Error al cerrar sesion",
                                        e
                                    )
                                } finally {
                                    isLoggingOut = false
                                }
                            }
                        }
                    }


                )
            }
        }
    }
}
