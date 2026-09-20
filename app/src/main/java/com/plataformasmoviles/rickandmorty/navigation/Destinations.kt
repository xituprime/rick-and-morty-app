package com.plataformasmoviles.rickandmorty.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginDestination

@Serializable
object CharactersDestination

@Serializable
data class CharacterDetailDestination(
    val id: Int
)