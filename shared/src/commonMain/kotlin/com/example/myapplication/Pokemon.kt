package com.example.myapplication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Pokemon(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,

    @Transient
    val id: String = url.split("https://pokeapi.co/api/v2/pokemon/").last().replace("/", ""),
    @Transient
    var frontImage: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
)

@Serializable
data class PokemonList(
    @SerialName("results")
    val results: List<Pokemon>,
    @SerialName("next")
    val next: String?
)

@Serializable
data class PokemonDetail(
    @SerialName("name")
    val name: String,
    @SerialName("sprites")
    val sprites: PokemonSprites,
    @SerialName("types")
    val types: List<PokemonTypeSlot>
)

@Serializable
data class PokemonSprites(
    @SerialName("front_default")
    val front: String
)
@Serializable
data class PokemonTypeSlot(
    @SerialName("type")
    val type: PokemonType
)

@Serializable
data class PokemonType(
    @SerialName("name")
    val type: String
)