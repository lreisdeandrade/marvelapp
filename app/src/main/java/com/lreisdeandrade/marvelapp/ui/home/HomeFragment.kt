package com.lreisdeandrade.marvelapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lreisdeandrade.marvelapp.AppContext
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelapp.ui.characterdetail.CharacterDetailActivity
import com.lreisdeandrade.marvelapp.ui.gone
import com.lreisdeandrade.marvelapp.ui.obtainViewModel
import com.lreisdeandrade.marvelapp.ui.visible
import com.lreisdeandrade.marvelservice.model.Character
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

private const val HAS_LOADED = "hasLoaded"
private const val CHARACTERS_RESULT = "charactersResult"

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var characters: ArrayList<Character>? = null
    private var hasLoaded = false

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            hasLoaded = savedInstanceState.getBoolean(HAS_LOADED, false)
            when (hasLoaded) {
                true -> {
                    characters = savedInstanceState.getParcelableArrayList(CHARACTERS_RESULT)
                    characters?.let { setupRecycler(it) }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasLoaded) {
            initViewModel()
        }
        charactersRecycler.adapter?.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(CHARACTERS_RESULT, characters)
        outState.putBoolean(HAS_LOADED, hasLoaded)
    }

    private fun initViewModel() {
        viewModel = obtainViewModel(AppContext.instance, HomeViewModel::class.java)

        with(viewModel) {
            hasErrorLive.observe(viewLifecycleOwner, Observer {
                when (it) {
                    true -> {

                    }
                }
            })

            isLoadingLive.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it) {
                        homeFragmentLoading.visible()
                    } else {
                        homeFragmentLoading.gone()
                    }
                }
            })

            characterLive.observe(viewLifecycleOwner, Observer {
                it?.let {
                    it.characterDataContainer.results?.let { characterList ->
                        setupRecycler(characterList)
                        hasLoaded = true
                    }
                    Timber.d(it.toString())
                }
            })
        }
        viewModel.loadCharactersList()
    }

    private fun setupRecycler(characterList: ArrayList<Character>?) {
        this.characters = characterList
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        charactersRecycler.layoutManager = linearLayoutManager
        charactersRecycler.isNestedScrollingEnabled = false

        characterList?.let {
            charactersRecycler.adapter =
                CharacterAdapter(it) { character, view ->
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