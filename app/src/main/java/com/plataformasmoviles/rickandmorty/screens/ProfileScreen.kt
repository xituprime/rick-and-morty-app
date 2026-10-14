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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plataformasmoviles.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun ProfileScreen(
    userName: String,
    onLogoutClick: () -> Unit
) {
    Scaffold { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Surface(
                modifier = Modifier.size(150.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier.padding(32.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(
                modifier = Modifier.height(48.dp)
            )

            ProfileInformationRow(
                label = "Nombre:",
                value = userName
            )

            ProfileInformationRow(
                label = "Carné:",
                value = "25783"
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Button(
                onClick = onLogoutClick
            ) {
                Text(
                    text = "Cerrar sesión"
                )
            }
        }
    }
}

@Composable
private fun ProfileInformationRow(
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
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ProfileScreenPreview() {
    RickAndMortyTheme {
        ProfileScreen(
            userName = "Axel Xitumul",
            onLogoutClick = {}
        )
    }
}