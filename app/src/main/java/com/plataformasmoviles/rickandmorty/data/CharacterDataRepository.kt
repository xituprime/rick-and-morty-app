package com.plataformasmoviles.rickandmorty.data

import com.plataformasmoviles.rickandmorty.data.local.CharacterDao
import com.plataformasmoviles.rickandmorty.data.local.CharacterEntity
import com.plataformasmoviles.rickandmorty.data.remote.RickAndMortyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.delay
import retrofit2.HttpException
import com.plataformasmoviles.rickandmorty.data.remote.retryOnRateLimit

class CharacterDataRepository(
    private val dao: CharacterDao,
    private val api: RickAndMortyApi
) {

    // Consultar los personajes almacenados en Room
    fun getCharacters(): Flow<List<CharacterEntity>> {
        return dao.getAllCharacters()
    }

    // Consultar un personaje por ID
    fun getCharacterById(id: Int): Flow<CharacterEntity?> {
        return dao.getCharacterById(id)
    }

    // Descargar todas las paginas de la API

    suspend fun synchronizeFromApi() {

        // Descargar la primera pagina
        val firstPage = retryOnRateLimit {
            api.getCharacters(page = 1)
        }

        val characters = firstPage.results.toMutableList()

        // Descargar las paginas restantes
        for (page in 2..firstPage.info.pages) {

            // Esperar medio segundo entre solicitudes
            delay(500)

            val response = retryOnRateLimit {
                api.getCharacters(page = page)
            }

            characters.addAll(response.results)
        }

        // Convertir los datos de la API a entidades Room
        val entities = characters.map { character ->
            CharacterEntity(
                id = character.id,
                name = character.name,
                species = character.species,
                status = character.status,
                image = character.image,
                gender = character.gender,
                isFavorite = false
            )
        }

        // Guardar todos los personajes en Room
        dao.insertCharacters(entities)
    }


    // Actualizar favorito
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    ) {
        dao.updateFavorite(id, isFavorite)
    }

    // Eliminar todos los personajes
    suspend fun deleteAll() {
        dao.deleteAllCharacters()
    }

    suspend fun createCharacter(
        name: String,
        species: String,
        status: String,
        image: String,
        gender: String
    ) {
        dao.createCharacter(
            name = name,
            species = species,
            status = status,
            image = image,
            gender = gender
        )
    }

    suspend fun updateCharacter(
        id: Int,
        name: String,
        species: String,
        status: String,
        image: String,
        gender: String
    ) {
        dao.updateCharacter(
            id = id,
            name = name,
            species = species,
            status = status,
            image = image,
            gender = gender
        )
    }

    suspend fun deleteCharacter(id: Int) {
        dao.deleteCharacter(id)
    }

}
