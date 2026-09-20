package com.plataformasmoviles.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.plataformasmoviles.rickandmorty.navigation.AppNavigation
import com.plataformasmoviles.rickandmorty.ui.theme.RickAndMortyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            RickAndMortyTheme {

                AppNavigation()
            }
        }
    }
}