package com.plataformasmoviles.rickandmorty.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.plataformasmoviles.rickandmorty.viewmodel.CharacterDetailUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    uiState: CharacterDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    onLoadingClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Characters details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
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

                uiState.data != null -> {
                    val character = uiState.data

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        AsyncImage(
                            model = character.image,
                            contentDescription = character.name,
                            modifier = Modifier
                                .size(220.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        CharacterDetailRow("Species:", character.species)
                        CharacterDetailRow("Status:", character.status)
                        CharacterDetailRow("Gender:", character.gender)
                    }
                }

                else -> {
                    ErrorContent(onRetryClick = onRetry)
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterDetailLoadingPreview() {
    MaterialTheme {
        CharacterDetailScreen(
            uiState = CharacterDetailUiState(),
            onBackClick = {},
            onRetry = {},
            onLoadingClick = {}
        )
    }
}