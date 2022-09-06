package com.example.myapplication.network

import com.example.myapplication.PokemonDetail
import com.example.myapplication.PokemonList
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class PokemonAPI {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            }
            serializer = KotlinxSerializer(json)
        }
    }

    companion object {
        private const val POKEDEX_ENDPOINT = "https://pokeapi.co/api/v2/"
    }

    @Throws(Throwable::class) suspend fun getFirstPagePokedex(): PokemonList {
        return httpClient.get(POKEDEX_ENDPOINT+"pokemon")
    }

    suspend fun getPokemon(url: String): PokemonDetail {
        return httpClient.get(url)
    }
}