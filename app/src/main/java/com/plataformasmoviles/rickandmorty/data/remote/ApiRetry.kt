package com.plataformasmoviles.rickandmorty.data.remote

import kotlinx.coroutines.delay
import retrofit2.HttpException

suspend fun <T> retryOnRateLimit(
    request: suspend () -> T
): T {
    var waitTime = 2000L

    repeat(4) { attempt ->
        try {
            return request()
        } catch (e: HttpException) {
            if (e.code() != 429 || attempt == 3) {
                throw e
            }

            delay(waitTime)
            waitTime *= 2
        }
    }

    error("No se pudo completar la solicitud")
}
