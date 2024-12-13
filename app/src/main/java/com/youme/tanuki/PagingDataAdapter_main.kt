package com.youme.tanuki

import android.content.res.Resources
import android.util.Log
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource

class MangaPagingAdapter(private val onItemClicker: (Manga) -> Unit) : PagingDataAdapter<Manga, MangaPagingAdapter.MangaViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mangabox, parent, false)
        return MangaViewHolder(view,onItemClicker)
    }

    class MangaViewHolder(itemView: View,private val onItemClicker: (Manga) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val flag: ImageView = itemView.findViewById(R.id.imageView2)
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val adult: TextView = itemView.findViewById(R.id.textView7)
        private val las_chapter: TextView = itemView.findViewById(R.id.textView2)
        fun bind(mangaItem: Manga) {
            if (mangaItem.countryOfOrigin=="JP") flag.setImageResource(R.drawable.japanflag)
            if (mangaItem.countryOfOrigin=="KR") flag.setImageResource(R.drawable.southkoreaflag)
            if (mangaItem.countryOfOrigin=="CN") flag.setImageResource(R.drawable.china)

            las_chapter.text=mangaItem.chapters.toString()
            val titl = mangaItem.title.english?:mangaItem.title.romaji
            textView.text = if (titl.length > 15) {
                "${titl.substring(0, 15)}..."
            } else {
                titl
            }
            if (mangaItem.isAdult == true){adult.visibility=View.VISIBLE}else {adult.visibility=View.GONE}
            if (mangaItem.chapters.toString().isBlank()){
                las_chapter.visibility=View.GONE
            }
            Glide.with(itemView.context).load(mangaItem.coverImage.large).into(imageView)
            itemView.setOnClickListener { onItemClicker(mangaItem) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Manga>() {
            override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean = oldItem.title == newItem.title
            override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean = oldItem == newItem
        }
    }
}
