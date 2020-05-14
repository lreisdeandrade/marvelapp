package com.lreisdeandrade.marvelapp.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lreisdeandrade.marvelapp.util.ui.extension.loadUrl
import com.lreisdeandrade.marvelapp.util.Constants.DOT
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelservice.model.Character
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoriteAdapter(
    private val items: List<Character>,
    private val listener: (Character, View) -> Unit
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) =
        holder.bind(items[position], listener)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Character, clickListener: (Character, View) -> Unit) = with(itemView) {
            favoriteCharacterName.text = item.name
            favoriteCharacterImage.loadUrl(item.thumbnail.path.plus(DOT.plus(item.thumbnail.extension)))
            itemView.setOnClickListener { clickListener(item, favoriteCharacterImage) }
        }
    }
}