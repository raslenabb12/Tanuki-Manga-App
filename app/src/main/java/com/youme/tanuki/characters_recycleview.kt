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

class MyAdapter(
    private val onItemClicker: (CharacterEdge) -> Unit
) : ListAdapter<CharacterEdge, MyAdapter.MyViewHolder>(CharactersDiffCallback) {
    private var originalList: List<CharacterEdge> = emptyList()

    class MyViewHolder(
        itemView: View,
        private val onItemClicker: (CharacterEdge) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textView: TextView = itemView.findViewById(R.id.textView8)
        private val role: TextView = itemView.findViewById(R.id.textView12)

        fun bind(character: CharacterEdge) {
            Glide.with(itemView.context).load(character.node?.image?.medium).into(imageView)
            textView.text=character.node?.name?.full.toString()
            role.text = character.role
            itemView.setOnClickListener { onItemClicker(character) }
        }

        fun resetScale() {
            itemView.scaleX = 1f
            itemView.scaleY = 1f
            itemView.alpha=1f
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.characterbox, parent, false)
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

    override fun submitList(list: List<CharacterEdge>?) {
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

}
