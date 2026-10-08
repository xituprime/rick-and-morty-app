package com.plataformasmoviles.rickandmorty.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingContent(
    onLoadingClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onLoadingClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()

        Text(
            text = "Cargando...",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ErrorContent(
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            text = "Error al obtener la información.\nIntenta de nuevo.",
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.error
        )

        Button(onClick = onRetryClick) {
            Text("Reintentar")
        }
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
private fun LoadingPreview() {
    MaterialTheme {
        LoadingContent(onLoadingClick = {})
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
private fun ErrorPreview() {
    MaterialTheme {
        ErrorContent(onRetryClick = {})
    }
}