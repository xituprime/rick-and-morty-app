package com.plataformasmoviles.rickandmorty.data.local

import com.plataformasmoviles.rickandmorty.model.Character

fun Character.toEntity(
    isFavorite: Boolean = false
): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        species = species,
        status = status,
        image = image,
        gender = gender,
        isFavorite = isFavorite
    )
}

fun CharacterEntity.toModel(): Character {
    return Character(
        id = id,
        name = name,
        species = species,
        status = status,
        image = image,
        gender = gender
    )
}
