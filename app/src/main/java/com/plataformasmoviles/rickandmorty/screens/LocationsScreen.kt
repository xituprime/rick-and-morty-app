package com.plataformasmoviles.rickandmorty.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plataformasmoviles.rickandmorty.components.ErrorContent
import com.plataformasmoviles.rickandmorty.components.LoadingContent
import com.plataformasmoviles.rickandmorty.model.Location
import com.plataformasmoviles.rickandmorty.viewmodel.LocationsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    uiState: LocationsUiState,
    onLocationClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onLoadingClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.hasError -> {
                    ErrorContent(onRetryClick = onRetry)
                }

                uiState.isLoading -> {
                    LoadingContent(onLoadingClick = onLoadingClick)
                }

                else -> {
                    LazyColumn {
                        items(
                            items = uiState.data,
                            key = { it.id }
                        ) { location ->
                            LocationItem(
                                location = location,
                                onClick = {
                                    onLocationClick(location.id)
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationItem(
    location: Location,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
    ) {
        Text(
            text = location.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = location.type,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationsLoadingPreview() {
    MaterialTheme {
        LocationsScreen(
            uiState = LocationsUiState(),
            onLocationClick = {},
            onRetry = {},
            onLoadingClick = {}
        )
    }
}