package com.plataformasmoviles.rickandmorty.data

import com.plataformasmoviles.rickandmorty.model.Character

object CharacterRepository {

    val characters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            species = "Human",
            status = "Alive",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            gender = "Male"
        ),
        Character(
            id = 2,
            name = "Morty Smith",
            species = "Human",
            status = "Alive",
            image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
            gender = "Male"
        ),
        Character(
            id = 3,
            name = "Summer Smith",
            species = "Human",
            status = "Alive",
            image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
            gender = "Female"
        ),
        Character(
            id = 4,
            name = "Beth Smith",
            species = "Human",
            status = "Alive",
            image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
            gender = "Female"
        ),
        Character(
            id = 5,
            name = "Jerry Smith",
            species = "Human",
            status = "Alive",
            image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
            gender = "Male"
        ),
        Character(
            id = 6,
            name = "Abadango Cluster Princess",
            species = "Alien",
            status = "Alive",
            image = "https://rickandmortyapi.com/api/character/avatar/6.jpeg",
            gender = "Female"
        ),
        Character(
            id = 7,
            name = "Abradolf Lincler",
            species = "Human",
            status = "Unknown",
            image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg",
            gender = "Male"
        ),
        Character(
            id = 8,
            name = "Adjudicator Rick",
            species = "Human",
            status = "Dead",
            image = "https://rickandmortyapi.com/api/character/avatar/8.jpeg",
            gender = "Male"
        ),
        Character(
            id = 9,
            name = "Agency Director",
            species = "Human",
            status = "Dead",
            image = "https://rickandmortyapi.com/api/character/avatar/9.jpeg",
            gender = "Male"
        ),
        Character(
            id = 10,
            name = "Alan Rails",
            species = "Human",
            status = "Dead",
            image = "https://rickandmortyapi.com/api/character/avatar/10.jpeg",
            gender = "Male"
        )
    )

    fun getCharacterById(id: Int): Character? {
        return characters.find { character ->
            character.id == id
        }
    }
}