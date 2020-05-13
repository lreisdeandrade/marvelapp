package com.lreisdeandrade.marvelapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lreisdeandrade.marvelapp.AppContext
import com.lreisdeandrade.marvelapp.ui.gone
import com.lreisdeandrade.marvelapp.ui.obtainViewModel
import com.lreisdeandrade.marvelapp.ui.visible
import com.lreisdeandrade.marvelservice.model.Character
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber
import android.view.*
import androidx.appcompat.widget.SearchView
import com.lreisdeandrade.marvelapp.ui.characterdetail.CharacterDetailActivity
import com.lreisdeandrade.marvelapp.util.PaginationScrollListener
import com.lreisdeandrade.marvellapp.R

private const val HAS_LOADED = "hasLoaded"
private const val CHARACTERS_RESULT = "charactersResult"
const val ITEMS_PER_PAGE = 20

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var charactersList: ArrayList<Character>? = null
    private var characterAdapter: CharacterAdapter? = null
    private var hasLoaded = false
    private var offset = 0
    private var currentPage = 0
    private var isLoading = false

    var searchView : SearchView?=null
    var query : String = ""

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            hasLoaded = savedInstanceState.getBoolean(HAS_LOADED, false)
            when (hasLoaded) {
                true -> {
                    charactersList = savedInstanceState.getParcelableArrayList(CHARACTERS_RESULT)
                    charactersList?.let { setupRecycler(it) }
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
        outState.putParcelableArrayList(CHARACTERS_RESULT, charactersList)
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
            isBottomLoadingLive.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it) {
                        homeBottomLoading.visible()
                    } else {
                        homeBottomLoading.gone()
                    }
                }
            })

            fetchCharacterLive.observe(viewLifecycleOwner, Observer {
                it?.let {
                    it.characterDataContainer.results?.let { characterList ->
                        charactersRecycler.adapter?.let {
                            (it as CharacterAdapter).add(characterList)
                        } ?: run {
                            hasLoaded = true
                            setupRecycler(characterList)
                        }
                        isLoading = false
                        hasLoaded = true
                    }
                    Timber.d(it.toString())
                }
            })

            characterSearchLive.observe(viewLifecycleOwner, Observer {
                it?.let {
                    characterAdapter?.filterList(it)
                }
            })
        }
        viewModel.loadCharactersList(offset)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.character_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query = newText
                viewModel.filterCharacter(query, charactersList)
                return false
            }
        })
    }

    private fun setupRecycler(characterList: ArrayList<Character>?) {
        this.charactersList = characterList
        val linearLayoutManager = LinearLayoutManager(context)
        charactersRecycler.layoutManager = linearLayoutManager
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        charactersRecycler.layoutManager = linearLayoutManager

        characterList?.let {
            characterAdapter = CharacterAdapter(it) { character, view ->
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

        charactersRecycler.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {

            override fun getTotalPageCount(): Int {
                return ITEMS_PER_PAGE
            }

            override fun loadMoreItems() {
                isLoading = true
                currentPage += 20
                viewModel.loadCharactersList(currentPage)
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }
}