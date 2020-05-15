package com.lreisdeandrade.marvelapp.ui.character

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lreisdeandrade.marvelapp.AppContext
import com.lreisdeandrade.marvelapp.util.ui.extension.gone
import com.lreisdeandrade.marvelapp.util.ui.extension.obtainViewModel
import com.lreisdeandrade.marvelapp.util.ui.extension.visible
import com.lreisdeandrade.marvelservice.model.Character
import kotlinx.android.synthetic.main.fragment_character.*
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.lreisdeandrade.marvelapp.ui.characterdetail.CharacterDetailActivity
import com.lreisdeandrade.marvelapp.util.ui.extension.showSnackBar
import com.lreisdeandrade.marvelapp.util.ui.PaginationScrollListener
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelservice.ITEMS_PER_PAGE

class CharacterFragment : Fragment() {

    private lateinit var viewModel: CharacterViewModel

    private var charactersList: ArrayList<Character>? = null
    private var characterAdapter: CharacterAdapter? = null
    private var hasLoaded = false
    private var currentPage = 0
    private var isLoading = false
    private var searchView: SearchView? = null

    companion object {
        fun newInstance(): CharacterFragment {
            return CharacterFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_character, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
//            hasLoaded = savedInstanceState.getBoolean(HAS_LOADED, false)
//            when (hasLoaded) {
//                true -> {
//                    charactersList = savedInstanceState.getParcelableArrayList(CHARACTERS_RESULT)
//                    charactersList?.let { setupRecycler(it) }
//                }
//            }
        }
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.character_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        val closeButton = searchView?.findViewById(R.id.search_close_btn) as ImageView

        closeButton.setOnClickListener {
            viewModel.filterCharacter("", charactersList)
            searchView?.onActionViewCollapsed()
            searchItem.collapseActionView()

        }
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterCharacter(newText, charactersList)
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
//        if (!hasLoaded) {
//        initViewModel()
//        }
        charactersRecycler.adapter?.notifyDataSetChanged()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putParcelableArrayList(CHARACTERS_RESULT, charactersList)
//        outState.putBoolean(HAS_LOADED, hasLoaded)
//    }

    private fun initViewModel() {
        viewModel = obtainViewModel(AppContext.instance, CharacterViewModel::class.java)

        with(viewModel) {
            hasErrorLive.observe(viewLifecycleOwner, Observer {
                when (it) {
                    true -> {
                        view?.showSnackBar(
                            getString(R.string.generic_error), Snackbar.LENGTH_LONG,
                            getString(R.string.try_again)
                        ) { viewModel.loadCharactersList(currentPage) }
                    }
                }
            })

            isLoadingLive.observe(viewLifecycleOwner, Observer
            {
                it?.let {
                    if (it) {
                        characterFragmentLoading.visible()
                    } else {
                        characterFragmentLoading.gone()
                    }
                }
            })
            isBottomLoadingLive.observe(viewLifecycleOwner, Observer
            {
                it?.let {
                    if (it) {
                        characterBottomLoading.visible()
                    } else {
                        characterBottomLoading.gone()
                    }
                }
            })

            fetchCharacterLive.observe(viewLifecycleOwner, Observer
            {
                it?.let { characterList ->
                    charactersRecycler.adapter?.let {

                        (it as CharacterAdapter).add(characterList.characterDataContainer.results!!)
                    } ?: run {
                        hasLoaded = true
                        setupRecycler(characterList.characterDataContainer.results!!)
                    }
                    isLoading = false
                    hasLoaded = true
                }
            })

            characterSearchLive.observe(viewLifecycleOwner, Observer
            {
                it?.let { characterAdapter?.filterList(it) }
            })

            hasErrorLive.observe(viewLifecycleOwner, Observer
            {
                if (it) {
                    showSnackbarError()
                }
            })
            hasErrorNoDataLive.observe(viewLifecycleOwner, Observer
            {
                when (it) {
                    true -> emptyView?.visible()
                    false -> emptyView?.gone()
                }
            })
        }
        viewModel.loadCharactersList(currentPage)
    }

    private fun showSnackbarError() {
        view?.showSnackBar(
            getString(R.string.generic_error), Snackbar.LENGTH_LONG,
            getString(R.string.try_again)
        ) { viewModel.loadCharactersList(currentPage) }
    }

    private fun setupRecycler(characterList: ArrayList<Character>?) {
        this.charactersList = characterList
        val linearLayoutManager = LinearLayoutManager(context)
        charactersRecycler.layoutManager = linearLayoutManager
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        charactersRecycler.layoutManager = linearLayoutManager

        characterList?.let {
            characterAdapter =
                CharacterAdapter(it) { character, view ->
                    context?.let { context ->
                        CharacterDetailActivity.createIntent(
                            context,
                            character,
                            view
                        )
                    }
                }
            charactersRecycler.adapter = characterAdapter
        }

        charactersRecycler.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {

            override fun getTotalPageCount(): Int {
                return ITEMS_PER_PAGE
            }

            override fun loadMoreItems() {
                isLoading = true
                currentPage += ITEMS_PER_PAGE
                viewModel.loadCharactersList(currentPage)
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }
}