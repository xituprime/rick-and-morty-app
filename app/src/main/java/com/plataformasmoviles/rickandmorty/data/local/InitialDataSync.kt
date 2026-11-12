package com.plataformasmoviles.rickandmorty.data.local

import com.plataformasmoviles.rickandmorty.data.CharacterDataRepository
import com.plataformasmoviles.rickandmorty.data.LocationDataRepository
import com.plataformasmoviles.rickandmorty.data.remote.RetrofitClient

class InitialDataSync(
    private val database: AppDatabase
) {

    private val characterRepository = CharacterDataRepository(
        dao = database.characterDao(),
        api = RetrofitClient.api
    )

    private val locationRepository = LocationDataRepository(
        dao = database.locationDao(),
        api = RetrofitClient.api
    )


    suspend fun synchronize() {
        characterRepository.synchronizeFromApi()
        locationRepository.synchronizeFromApi()
    }
}
