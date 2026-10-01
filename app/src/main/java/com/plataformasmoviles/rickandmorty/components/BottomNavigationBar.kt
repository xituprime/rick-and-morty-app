package com.plataformasmoviles.rickandmorty.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.plataformasmoviles.rickandmorty.ui.theme.RickAndMortyTheme

enum class BottomDestination {
    CHARACTERS,
    LOCATIONS,
    PROFILE
}

@Composable
fun BottomNavigationBar(
    selectedDestination: BottomDestination,
    onCharactersClick: () -> Unit,
    onLocationsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {

        NavigationBarItem(
            selected = selectedDestination == BottomDestination.CHARACTERS,
            onClick = onCharactersClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Characters"
                )
            },
            label = {
                Text("Characters")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.onSurface,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        NavigationBarItem(
            selected = selectedDestination == BottomDestination.LOCATIONS,
            onClick = onLocationsClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Locations"
                )
            },
            label = {
                Text("Locations")
            }
        )

        NavigationBarItem(
            selected = selectedDestination == BottomDestination.PROFILE,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
                )
            },
            label = {
                Text("Profile")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarPreview() {
    RickAndMortyTheme {
        BottomNavigationBar(
            selectedDestination = BottomDestination.CHARACTERS,
            onCharactersClick = {},
            onLocationsClick = {},
            onProfileClick = {}
        )
    }
}