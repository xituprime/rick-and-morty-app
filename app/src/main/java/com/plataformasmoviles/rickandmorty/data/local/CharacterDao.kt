
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

    // Crear un personaje local
    @Query("""
        INSERT INTO characters
        (id, name, species, status, image, gender, isFavorite)
        VALUES (
            (SELECT MIN(COALESCE(MIN(id), 0) - 1, -1)
             FROM characters),
            :name, :species, :status, :image, :gender, 0
        )
    """)
    suspend fun createCharacter(
        name: String,
        species: String,
        status: String,
        image: String,
        gender: String
    )

    // Actualizar un personaje existente
    @Query("""
        UPDATE characters
        SET name = :name,
            species = :species,
            status = :status,
            image = :image,
            gender = :gender
        WHERE id = :id
    """)
    suspend fun updateCharacter(
        id: Int,
        name: String,
        species: String,
        status: String,
        image: String,
        gender: String
    )

    // Eliminar un personaje por ID
    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteCharacter(id: Int)

}
