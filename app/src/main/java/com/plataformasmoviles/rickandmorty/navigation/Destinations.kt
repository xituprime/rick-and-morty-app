package com.plataformasmoviles.rickandmorty.navigation

import kotlinx.serialization.Serializable

// -------------------------
// Navegación principal
// -------------------------

@Serializable
object LoginDestination

@Serializable
object MainDestination

@Serializable
object SyncDestination


// -------------------------
// Bottom Navigation
// -------------------------

@Serializable
object CharactersGraph

@Serializable
object LocationsGraph

@Serializable
object ProfileDestination


// -------------------------
// Characters nested graph
// -------------------------

@Serializable
object CharactersDestination

@Serializable
data class CharacterDetailDestination(
    val id: Int
)


// -------------------------
// Locations nested graph
// -------------------------

@Serializable
object LocationsDestination

@Serializable
data class LocationDetailDestination(
    val id: Int
)