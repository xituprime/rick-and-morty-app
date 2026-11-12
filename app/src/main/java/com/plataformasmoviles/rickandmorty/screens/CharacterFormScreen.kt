package com.plataformasmoviles.rickandmorty.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plataformasmoviles.rickandmorty.model.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    character: Character? = null,
    onSave: (
        String, String, String, String, String
    ) -> Unit,
    onCancel: () -> Unit
) {
    var name by rememberSaveable(character?.id) {
        mutableStateOf(character?.name.orEmpty())
    }

    var species by rememberSaveable(character?.id) {
        mutableStateOf(character?.species.orEmpty())
    }

    var status by rememberSaveable(character?.id) {
        mutableStateOf(character?.status.orEmpty())
    }

    var image by rememberSaveable(character?.id) {
        mutableStateOf(character?.image.orEmpty())
    }

    var gender by rememberSaveable(character?.id) {
        mutableStateOf(character?.gender.orEmpty())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (character == null)
                            "Nuevo personaje"
                        else
                            "Editar personaje"
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = species,
                onValueChange = { species = it },
                label = { Text("Especie") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Estado") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Género") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = image,
                onValueChange = { image = it },
                label = { Text("URL de imagen") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onSave(
                        name.trim(),
                        species.trim(),
                        status.trim(),
                        image.trim(),
                        gender.trim()
                    )
                },
                enabled = name.isNotBlank() &&
                        species.isNotBlank() &&
                        status.isNotBlank() &&
                        gender.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (character == null)
                        "Crear personaje"
                    else
                        "Guardar cambios"
                )
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
