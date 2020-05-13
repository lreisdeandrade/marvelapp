package com.lreisdeandrade.marvelapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lreisdeandrade.marvelapp.AppContext
import com.lreisdeandrade.marvelapp.ui.characterdetail.CharacterDetailActivity
import com.lreisdeandrade.marvelapp.ui.gone
import com.lreisdeandrade.marvelapp.ui.obtainViewModel
import com.lreisdeandrade.marvelapp.ui.visible
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelservice.model.Character
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteList: ArrayList<Character>

    companion object {
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavoritesCharacters()
    }

    private fun initViewModel() {
        viewModel = obtainViewModel(AppContext.instance, FavoriteViewModel::class.java)
        with(viewModel) {
            isLoadingLive.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it) {
                        favoriteFragmentLoading.visible()
                    } else {
                        favoriteFragmentLoading.gone()
                    }
                }
            })

            listFavoritesLive.observe(viewLifecycleOwner, Observer {
                favoriteList = it
                isEmptyListLive(it)
            })

            isEmptyListLive.observe(viewLifecycleOwner, Observer {
                if (it) {
                    favoriteCharactersRecycler.gone()
                    errorView.visible()
                } else {
                    favoriteCharactersRecycler.visible()
                    errorView.gone()
                    setupFavoriteListCharacter(favoriteList)
                }
            })
        }
    }

    private fun setupFavoriteListCharacter(it: ArrayList<Character>) {
        val linearLayoutManager = GridLayoutManager(context, 3)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        favoriteCharactersRecycler.layoutManager = linearLayoutManager
        favoriteCharactersRecycler.isNestedScrollingEnabled = false

        it?.let {
            favoriteCharactersRecycler.adapter =
                FavoriteAdapter(it) { character, view ->
                    context?.let { context ->
                        CharacterDetailActivity.createIntent(
                            context,
                            character,
                            view
                        )
                    }
                }
        }
    }
}