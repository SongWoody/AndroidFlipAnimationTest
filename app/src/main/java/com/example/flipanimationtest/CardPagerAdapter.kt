package com.example.flipanimationtest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flipanimationtest.databinding.ViewCardBinding

class CardPagerAdapter(
    var items: List<String> = emptyList()
): RecyclerView.Adapter<CardHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        return CardHolder(
            ViewCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        holder.bind(items.getOrNull(position) ?: "empty")
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class CardHolder(private val binding: ViewCardBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        binding.root.tag = item
        binding.textView.text = item
    }
}