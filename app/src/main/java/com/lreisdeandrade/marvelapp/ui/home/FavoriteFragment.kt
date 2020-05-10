package com.lreisdeandrade.marvelapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelservice.model.Character

class FavoriteFragment : Fragment() {

    //    private lateinit var viewModel: GenreViewModel
    private lateinit var characters: List<Character>
//    private lateinit var moviesAdapter: MoviesByGenreAdapter

    companion object {
        fun newInstance(): FavoriteFragment {
            val fragment = FavoriteFragment()
//            fragment.arguments = Bundle().apply {
//                putSerializable("teste", characters)
//            }
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initViews()
        initViewModel()
    }

    private fun initData() {

//        arguments?.getSerializable("teste")?.let {
//            characters = it
//        } ?: run {
//            activity?.requiredBundleNotFound(EXTRA_GENRE)
//        }
    }

    private fun initViewModel() {
//        viewModel = obtainViewModel(AppContext.instance, GenreViewModel::class.java)
//
//        with(viewModel) {
//            hasErrorLive.observe(this@GenreFragment, Observer {
//                when (it) {
//                    true -> {
//
//                    }
//                }
//            })
//
//            moviesByGenreLive.observe(this@GenreFragment, Observer {
//                it?.let {
//                    setupMoviesByGenreAdapter(it)
//                }
//            })
//        }
//
//        viewModel.loadMoviesByGenre(genre.id)
    }

//    private fun setupMoviesByGenreAdapter(movieByGenreResponse: MovieByGenreResponse) {

//        moviesRecycler.setHasFixedSize(true)
//        val linearLayoutManager = LinearLayoutManager(context)
//        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        moviesRecycler.layoutManager = linearLayoutManager
//        moviesRecycler.isNestedScrollingEnabled = false
//
//        moviesRecycler.adapter = MoviesByGenreAdapter(movieByGenreResponse.results, {
//
//            MovieActivity.createIntent(activity as Context, it.id)
//        })
//    }

    private fun initViews() {
//        genreName.text = genre.name
    }
}