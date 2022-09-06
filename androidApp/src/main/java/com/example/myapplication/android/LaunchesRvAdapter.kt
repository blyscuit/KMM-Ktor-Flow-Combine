package com.example.myapplication.android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Pokemon
import com.nostra13.universalimageloader.core.ImageLoader

class LaunchesRvAdapter(var launches: List<Pokemon>) : RecyclerView.Adapter<LaunchesRvAdapter.LaunchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.item_launch, parent, false)
            .run(::LaunchViewHolder)
    }

    override fun getItemCount(): Int = launches.count()

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bindData(launches[position])
    }

    inner class LaunchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val missionNameTextView = itemView.findViewById<TextView>(R.id.missionName)
        private val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        private val imageLoader: ImageLoader = ImageLoader.getInstance() // Get singleton instance

        fun bindData(launch: Pokemon) {
            val ctx = itemView.context
            imageView.setImageDrawable(null)
            missionNameTextView.text = launch.name
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("url", launch.url)
                itemView.context.startActivity(intent)
            }
            imageLoader.displayImage(launch.frontImage, imageView)
        }
    }
}