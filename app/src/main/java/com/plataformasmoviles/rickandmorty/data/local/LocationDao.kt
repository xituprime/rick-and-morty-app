
package com.plataformasmoviles.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    // Obtener todas las ubicaciones
    @Query("SELECT * FROM locations ORDER BY id ASC")
    fun getAllLocations(): Flow<List<LocationEntity>>

    // Obtener una ubicación por ID
    @Query("SELECT * FROM locations WHERE id = :id LIMIT 1")
    fun getLocationById(id: Int): Flow<LocationEntity?>

    // Insertar ubicaciones
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(
        locations: List<LocationEntity>
    )

    // Eliminar todas las ubicaciones
    @Query("DELETE FROM locations")
    suspend fun deleteAllLocations()

    // Contar ubicaciones guardadas
    @Query("SELECT COUNT(*) FROM locations")
    suspend fun getLocationCount(): Int
}
