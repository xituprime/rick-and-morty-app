
package com.plataformasmoviles.rickandmorty.data.local

import com.plataformasmoviles.rickandmorty.model.Location

fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}

fun LocationEntity.toModel(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}
