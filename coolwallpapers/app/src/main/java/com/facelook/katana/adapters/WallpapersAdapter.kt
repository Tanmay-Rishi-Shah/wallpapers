package com.facelook.katana.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
import com.facelook.katana.R
import com.facelook.katana.models.ImageData
class WallpapersAdapter(private val images: List<ImageData>)
    : RecyclerView.Adapter<WallpapersAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageViewWallpaper)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wallpaper, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageData = images[position]
        Glide.with(holder.itemView.context)
            .load(imageData.url)
            .into(holder.imageView)
    }
    override fun getItemCount() = images.size
}
