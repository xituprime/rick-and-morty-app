
package com.plataformasmoviles.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    // Obtener todos los personajes
    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    // Obtener un personaje por ID
    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    fun getCharacterById(id: Int): Flow<CharacterEntity?>

    // Obtener solamente los favoritos
    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>

    // Insertar personajes
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacters(
        characters: List<CharacterEntity>
    )

    // Actualizar el estado de favorito por ID
    @Query(
        "UPDATE characters SET isFavorite = :isFavorite WHERE id = :id"
    )
    suspend fun updateFavorite(
        id: Int,
        isFavorite: Boolean
    )

    // Eliminar todos los personajes
    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()

    // Contar personajes guardados
    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharacterCount(): Int
}
