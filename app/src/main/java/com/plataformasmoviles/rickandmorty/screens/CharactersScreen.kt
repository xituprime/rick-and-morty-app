package com.plataformasmoviles.rickandmorty.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.plataformasmoviles.rickandmorty.components.ErrorContent
import com.plataformasmoviles.rickandmorty.components.LoadingContent
import com.plataformasmoviles.rickandmorty.model.Character
import com.plataformasmoviles.rickandmorty.viewmodel.CharactersUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    uiState: CharactersUiState,
    onCharacterClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onLoadingClick: () -> Unit,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
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
                        ) { character ->
                            CharacterItem(
                                character = character,
                                onClick = {
                                    onCharacterClick(character.id)
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
private fun CharacterItem(
    character: Character,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${character.species} - ${character.status}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = character.gender,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CharactersLoadingPreview() {
    MaterialTheme {
        CharactersScreen(
            uiState = CharactersUiState(),
            onCharacterClick = {},
            onRetry = {},
            onLoadingClick = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharactersErrorPreview() {
    MaterialTheme {
        CharactersScreen(
            uiState = CharactersUiState(
                isLoading = false,
                hasError = true
            ),
            onCharacterClick = {},
            onRetry = {},
            onLoadingClick = {},
            onBack = {}
        )
    }
}