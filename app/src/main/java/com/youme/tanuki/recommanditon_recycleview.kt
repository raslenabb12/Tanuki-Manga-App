package com.youme.tanuki
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.ListAdapter

class MyAdapter2(
    private val onItemClicker: (RecommendationNode) -> Unit
) : ListAdapter<RecommendationNode, MyAdapter2.MyViewHolder>(recommantionDiffCallback) {
    private var originalList: List<RecommendationNode> = emptyList()
    class MyViewHolder(
        itemView: View,
        private val onItemClicker: (RecommendationNode) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val chapter: TextView = itemView.findViewById(R.id.textView2)
        private val flag: ImageView = itemView.findViewById(R.id.imageView2)
        fun bind(manga: RecommendationNode) {
            if (manga.mediaRecommendation?.countryOfOrigin=="JP") flag.setImageResource(R.drawable.japanflag)
            if (manga.mediaRecommendation?.countryOfOrigin=="KR") flag.setImageResource(R.drawable.southkoreaflag)
            if (manga.mediaRecommendation?.countryOfOrigin=="CN") flag.setImageResource(R.drawable.china)
            chapter.visibility=View.GONE
            val titl = manga.mediaRecommendation?.title?.english?:manga.mediaRecommendation?.title?.romaji
            textView.text = if (titl.toString().length > 15) {
                "${titl.toString().substring(0, 15)}..."
            } else {
                titl
            }
            Glide.with(itemView.context).load(manga.mediaRecommendation?.coverImage?.large).into(imageView)
            itemView.setOnClickListener { onItemClicker(manga) }
        }

        fun resetScale() {
            itemView.scaleX = 1f
            itemView.scaleY = 1f
            itemView.alpha=1f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mangabox, parent, false)
        return MyViewHolder(view, onItemClicker)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val kdrama = getItem(position)
        holder.bind(kdrama)
        if (position < 12) {
            holder.resetScale()
        } else {
            holder.resetScale()
            holder.itemView.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .alpha(0.6f)
                .setDuration(0)
                .withEndAction {
                    holder.itemView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .setDuration(200)
                        .start()
                }
                .start()
        }
    }

    override fun submitList(list: List<RecommendationNode>?) {
        val initialLoad = list?.take(20)
        super.submitList(initialLoad)
        originalList = list ?: emptyList()
        Handler(Looper.getMainLooper()).postDelayed({
            super.submitList(originalList)
        }, 500)
    }
    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
    }
    object recommantionDiffCallback : DiffUtil.ItemCallback<RecommendationNode>() {
        override fun areItemsTheSame(oldItem: RecommendationNode, newItem: RecommendationNode): Boolean {
            return oldItem.mediaRecommendation?.title == newItem.mediaRecommendation?.title
        }
        override fun areContentsTheSame(oldItem: RecommendationNode, newItem: RecommendationNode): Boolean {
            return oldItem == newItem
        }
    }
}
