package com.example.myapplication

import com.example.myapplication.network.PokemonAPI
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*

class PokemonSDK() {

    sealed class PokemonFlow {
        object Dex : PokemonFlow()
        class Detail(val url: String) : PokemonFlow()
    }

    private val api = PokemonAPI()

    fun getPokemon(): Flow<PokemonList> {
        return flow {
            emit(api.getFirstPagePokedex())
        }
    }

    fun getPokemonDetail(url: String): Flow<PokemonDetail> {
        return flow {
            emit(api.getPokemon(url))
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    @Throws(Exception::class)
    suspend fun <T> getFlowIOS(flow: PokemonFlow, collector: FlowCollector<T>) {
        when(flow) {
            is PokemonFlow.Dex -> getPokemon().collect(collector as FlowCollector<PokemonList>)
            is PokemonFlow.Detail -> getPokemonDetail(flow.url).collect(collector as FlowCollector<PokemonDetail>)
        }
    }
}
