package com.plataformasmoviles.rickandmorty.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): ApiResponse<CharacterDto>

    @GET("location")
    suspend fun getLocations(
        @Query("page") page: Int = 1
    ): ApiResponse<LocationDto>
}
