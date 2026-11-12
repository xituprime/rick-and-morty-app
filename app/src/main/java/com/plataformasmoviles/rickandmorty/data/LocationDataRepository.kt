package com.plataformasmoviles.rickandmorty.data

import com.plataformasmoviles.rickandmorty.data.local.LocationDao
import com.plataformasmoviles.rickandmorty.data.local.LocationEntity
import com.plataformasmoviles.rickandmorty.data.remote.RickAndMortyApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.delay
import com.plataformasmoviles.rickandmorty.data.remote.retryOnRateLimit

class LocationDataRepository(
    private val dao: LocationDao,
    private val api: RickAndMortyApi
) {

    // Obtener ubicaciones desde Room
    fun getLocations(): Flow<List<LocationEntity>> {
        return dao.getAllLocations()
    }

    // Obtener una ubicacion por ID
    fun getLocationById(id: Int): Flow<LocationEntity?> {
        return dao.getLocationById(id)
    }

    // Descargar todas las paginas de la API

    suspend fun synchronizeFromApi() {

        // Descargar la primera pagina
        val firstPage = retryOnRateLimit {
            api.getLocations(page = 1)
        }

        val locations = firstPage.results.toMutableList()

        // Descargar las paginas restantes
        for (page in 2..firstPage.info.pages) {

            delay(500)

            val response = retryOnRateLimit {
                api.getLocations(page = page)
            }

            locations.addAll(response.results)
        }

        // Convertir las ubicaciones a entidades Room
        val entities = locations.map { location ->
            LocationEntity(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension,
                isFavorite = false
            )
        }

        // Guardar las ubicaciones en Room
        dao.insertLocations(entities)
    }


    // Eliminar todas las ubicaciones
    suspend fun deleteAll() {
        dao.deleteAllLocations()
    }
}
