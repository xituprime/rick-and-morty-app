package com.plataformasmoviles.rickandmorty.data.local

import com.plataformasmoviles.rickandmorty.data.CharacterRepository
import com.plataformasmoviles.rickandmorty.data.LocationDb
import kotlinx.coroutines.delay

class InitialDataSync(
    private val database: AppDatabase
) {

    suspend fun synchronize() {

        // Simular una carga de 4 segundos
        delay(4000)

        val characterDao = database.characterDao()
        val locationDao = database.locationDao()

        // Insertar personajes si la tabla esta vacia
        if (characterDao.getCharacterCount() == 0) {

            val characters = CharacterRepository.characters
                .map { character ->
                    character.toEntity()
                }

            characterDao.insertCharacters(characters)
        }

        // Insertar ubicaciones si la tabla esta vacia
        if (locationDao.getLocationCount() == 0) {

            val locations = LocationDb()
                .getAllLocations()
                .map { location ->
                    location.toEntity()
                }

            locationDao.insertLocations(locations)
        }
    }
}
