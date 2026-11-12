package com.plataformasmoviles.rickandmorty.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plataformasmoviles.rickandmorty.components.ErrorContent
import com.plataformasmoviles.rickandmorty.components.LoadingContent
import com.plataformasmoviles.rickandmorty.viewmodel.LocationFilter
import com.plataformasmoviles.rickandmorty.viewmodel.LocationsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    uiState: LocationsUiState,
    onLocationClick: (Int) -> Unit,
    onFavoriteClick: (Int) -> Unit,
    onFilterClick: (LocationFilter) -> Unit,
    onRetry: () -> Unit,
    onLoadingClick: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Locations") }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary))
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(LocationFilter.ALL to "Todas", LocationFilter.FAVORITES to "Favoritas", LocationFilter.NOT_FAVORITES to "No favoritas").forEach { (filter, title) ->
                    FilterChip(
                        selected = uiState.filter == filter,
                        onClick = { onFilterClick(filter) },
                        label = { Text(title, maxLines = 1) }
                    )
                }
            }
            when {
                uiState.hasError -> ErrorContent(onRetryClick = onRetry)
                uiState.isLoading -> LoadingContent(onLoadingClick = onLoadingClick)
                uiState.data.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay ubicaciones para mostrar")
                }
                else -> LazyColumn {
                    items(uiState.data, key = { it.id }) { location ->
                        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f).clickable { onLocationClick(location.id) }) {
                                Text(location.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(location.type, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { onFavoriteClick(location.id) }) {
                                val favorite = location.id in uiState.favoriteIds
                                Icon(if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = if (favorite) "Quitar favorito" else "Agregar favorito",
                                    tint = if (favorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationsLoadingPreview() {
    MaterialTheme {
        LocationsScreen(LocationsUiState(), {}, {}, {}, {}, {})
    }
}
