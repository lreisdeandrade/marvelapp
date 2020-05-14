package com.lreisdeandrade.marvelapp.ui.character

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.lreisdeandrade.marvelapp.AppContext
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelapp.util.ui.extension.loadUrl
import com.lreisdeandrade.marvelapp.util.Constants.CHARACTER_FAVORITE_TAG
import com.lreisdeandrade.marvelapp.util.Constants.CHARACTER_UNFAVORITE_TAG
import com.lreisdeandrade.marvelapp.util.scheduler.SchedulerProvider
import com.lreisdeandrade.marvelservice.model.Character
import io.reactivex.Observable
import kotlinx.android.synthetic.main.character_item.view.*
import timber.log.Timber

class CharacterAdapter(
    private var items: MutableList<Character>,
    private val itemListener: (Character, View) -> Unit
) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) = holder.bind(
        items[position], itemListener
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.character_item, parent, false)
        return CharacterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun filterList(filteredList: ArrayList<Character>) {
        items = filteredList
        notifyDataSetChanged()
    }

    internal fun add(characters: ArrayList<Character>) {
        val adapterSize = items.size
        items.addAll(characters)
        notifyItemRangeInserted(adapterSize, characters.size)
    }

    class CharacterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(
            item: Character,
            clickListener: (Character, View) -> Unit
        ) = with(itemView) {
            characterName.text = item.name
            characterImage.loadUrl(item.thumbnail.path.plus(".".plus(item.thumbnail.extension)))
            itemView.setOnClickListener { clickListener(item, characterImage) }

            verifyIsFavorite(
                item,
                characterFavoriteButton
            )
        }
    }
}

fun verifyIsFavorite(character: Character, button: Button) {
    Observable.just(0)
        .map { AppContext.instance.database.characterDao().findById(character.id) != null }
        .subscribeOn(SchedulerProvider.io())
        .observeOn(SchedulerProvider.ui())
        .subscribe({
            it
            when (it) {
                true -> {
                    button.tag = CHARACTER_FAVORITE_TAG
                    button.setBackgroundResource(R.drawable.ic_favorite_filled)
                }
                false -> {
                    button.tag = CHARACTER_UNFAVORITE_TAG
                    button.setBackgroundResource(R.drawable.ic_favorite)
                }
            }
            button.setOnClickListener {
                doFavoriteAction(character, button)
            }
        }, {
            Timber.e(it, "AdapterCheckFavoriteCharacter: %s", it.message)
        })
}

private fun doFavoriteAction(character: Character, button: Button) {
    if (button.tag == CHARACTER_FAVORITE_TAG) {
        removeFavoriteCharacter(character, button)
        button.tag = CHARACTER_UNFAVORITE_TAG
    } else {
        saveFavoriteCharacter(character, button)
        button.tag = CHARACTER_FAVORITE_TAG
    }
}

private fun saveFavoriteCharacter(character: Character, button: Button) {
    Observable.just(0)
        .map { AppContext.instance.database.characterDao().insert(character) }
        .subscribeOn(SchedulerProvider.io())
        .observeOn(SchedulerProvider.ui())
        .subscribe({
            button.setBackgroundResource(R.drawable.ic_favorite_filled)
        }, {
            Timber.e(it, "AdapterSaveFavoriteCharacter: %s", it.message)
        })
}

private fun removeFavoriteCharacter(character: Character, button: Button) {
    Observable.just(0)
        .map { AppContext.instance.database.characterDao().delete(character) }
        .subscribeOn(SchedulerProvider.io())
        .observeOn(SchedulerProvider.ui())
        .subscribe({
            button.setBackgroundResource(R.drawable.ic_favorite)
        }, {
            Timber.e(it, "AdapterDeleteFavoriteCharacter: %s", it.message)
        })
}


