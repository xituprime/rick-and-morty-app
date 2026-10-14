
package com.plataformasmoviles.rickandmorty.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.plataformasmoviles.rickandmorty.R

@Composable
fun LoginScreen(
    onStartClick: (String) -> Unit
) {
    var userName by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.rick_and_morty_logo
                ),
                contentDescription = "Rick and Morty Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Nombre de usuario") },
                placeholder = { Text("Ingresa tu nombre") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onStartClick(userName.trim())
                },
                enabled = userName.isNotBlank(),
                modifier = Modifier
                    .width(220.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Empezar")
            }
        }

        Text(
            text = "Axel Xitumul - 25783 ",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 29.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
