package com.youme.tanuki
import androidx.recyclerview.widget.DiffUtil

object CharactersDiffCallback : DiffUtil.ItemCallback<CharacterEdge>() {
    override fun areItemsTheSame(oldItem: CharacterEdge, newItem: CharacterEdge): Boolean {
        return oldItem.node?.name == newItem.node?.name
    }
    override fun areContentsTheSame(oldItem: CharacterEdge, newItem: CharacterEdge): Boolean {
        return oldItem == newItem
    }
}