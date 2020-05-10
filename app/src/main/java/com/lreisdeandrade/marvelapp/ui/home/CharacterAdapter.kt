package com.lreisdeandrade.marvelapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelapp.ui.loadUrl
import com.lreisdeandrade.marvelservice.model.Character
import kotlinx.android.synthetic.main.character_item.view.*

class CharacterAdapter(
    private val items: List<Character>,
    private val listener: (Character, View) -> Unit) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(items[position], clickListener)
//    }
override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) = holder.bind(items[position], listener)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return CharacterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Character, clickListener: (Character, View) -> Unit) = with(itemView) {
            characterName.text = item.name
            characterImage.loadUrl(item.thumbnail.path.plus(".".plus(item.thumbnail.extension)))
            itemView.setOnClickListener { clickListener(item,characterImage) }
        }
    }
}