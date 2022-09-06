package com.example.myapplication.android

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.myapplication.PokemonSDK
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DetailActivity: AppCompatActivity() {
    private lateinit var pokemonNameView: TextView
    private lateinit var pokemonTypeView: TextView
    private lateinit var pokemonImageView: ImageView
    private lateinit var progressBarView: FrameLayout

    private val sdk = PokemonSDK()

    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent

        setContentView(R.layout.detail_main)

        pokemonNameView = findViewById(R.id.pokemonNameText)
        pokemonTypeView = findViewById(R.id.pokemonTypeText)
        pokemonImageView = findViewById(R.id.imageView)
        progressBarView = findViewById(R.id.progressBar)

        if (intent != null) {
            val url = intent.getStringExtra("url")
            displayLaunches(url.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    private fun displayLaunches(url: String) {
        progressBarView.isVisible = true
        mainScope.launch {
            kotlin.runCatching {
                sdk.getPokemonDetail(url)
                    .catch {
                        Toast.makeText(this@DetailActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                    .collect {
                        title = it.name
                        pokemonNameView.text = it.name
                        pokemonTypeView.text = it.types
                            .map {
                                it.type.type
                            }
                            .joinToString(", ")
                        val imageLoader = ImageLoader.getInstance()
                        imageLoader.displayImage(it.sprites.front, pokemonImageView)
                    }
                progressBarView.isVisible = false
            }
        }
    }
}