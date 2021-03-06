package com.lreisdeandrade.marvelapp.ui.characterdetail

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.lreisdeandrade.marvelapp.AppContext
import com.lreisdeandrade.marvelapp.util.ui.extension.obtainViewModel
import com.lreisdeandrade.marvelapp.util.ui.extension.requiredBundleNotFound
import com.lreisdeandrade.marvelapp.util.Constants.BUNDLE_EXTRA_CHARACTER
import com.lreisdeandrade.marvelapp.util.Constants.CHARACTER_IMAGE_TRANSITION
import com.lreisdeandrade.marvelservice.model.Character
import kotlinx.android.synthetic.main.activity_character_detail.*
import kotlinx.android.synthetic.main.content_character_detail.*
import org.jetbrains.anko.intentFor
import com.bumptech.glide.Glide
import com.lreisdeandrade.marvelapp.util.Constants.DOT
import com.lreisdeandrade.marvelapp.util.ui.extension.loadGif
import com.lreisdeandrade.marvelapp.util.ui.extension.loadUrl
import com.lreisdeandrade.marvellapp.R

class CharacterDetailActivity : AppCompatActivity() {

    private lateinit var menu: Menu
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var character: Character
    private lateinit var viewModel: DetailViewModel
    private var isFavorite: Boolean = false
    private var menuItemFavorite: MenuItem? = null

    companion object {
        @JvmStatic
        fun createIntent(context: Context, character: Character, transitionView: View) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity, transitionView,
                CHARACTER_IMAGE_TRANSITION
            )
            context.startActivity(
                context.intentFor<CharacterDetailActivity>()
                    .putExtra(BUNDLE_EXTRA_CHARACTER, character), options.toBundle()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initViewModel()
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.character_detail_menu, menu)
        menuItemFavorite = menu.findItem(R.id.action_favorite)
        setupFavoriteButton(isFavorite)
        hideOption(R.id.action_favorite)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_favorite) {
            when (isFavorite) {
                true -> viewModel.removeFavoriteCharacter(character)
                false -> viewModel.saveFavoriteCharacter(character)
            }
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun initData() {
        with(intent) {
            getParcelableExtra<Character>(BUNDLE_EXTRA_CHARACTER)?.let {
                character = it
            } ?: run {
                requiredBundleNotFound(BUNDLE_EXTRA_CHARACTER)
            }
        }
    }

    private fun initViewModel() {
        viewModel = obtainViewModel(AppContext.instance, DetailViewModel::class.java)

        viewModel.apply {
            isFavoriteLive.observe(this@CharacterDetailActivity, Observer {
                it?.let {
                    setupFavoriteButton(it)
                }
            })
            checkFavoriteCharacter(character.id)
        }
    }

    private fun initViews() {
        setContentView(R.layout.activity_character_detail)

        setSupportActionBar(toolbar)
        collapsingToolbar = findViewById(R.id.toolbar_layout)

        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    showOption(R.id.action_favorite)
                } else if (isShow) {
                    isShow = false
                    hideOption(R.id.action_favorite)
                }
            }
        })

        fabFavorite.setOnClickListener {
            when (isFavorite) {
                true -> viewModel.removeFavoriteCharacter(character)
                false -> viewModel.saveFavoriteCharacter(character)
            }
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
        setupCharacterScreen()
    }

    private fun hideOption(id: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val item = menu.findItem(id)
        item.isVisible = false
    }

    private fun showOption(id: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val item = menu.findItem(id)
        item.isVisible = true
    }

    private fun setupFavoriteButton(it: Boolean) {
        isFavorite = it
        when (it) {
            true -> {
                fabFavorite.setImageResource(R.drawable.ic_favorite_filled)
                menuItemFavorite?.let {
                    it.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_filled)
                }
            }
            false -> {
                fabFavorite.setImageResource(R.drawable.ic_favorite)
                menuItemFavorite?.let {
                    it.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
                }
            }
        }
    }

    private fun setupCharacterScreen() {
        with(character) {
            characterDetailImage.loadUrl(thumbnail.path.plus(DOT.plus(thumbnail.extension)))
            collapsingToolbar.title = character.name
            if (description.isBlank()) {
                characterDescription.text =
                    getString(R.string.character_detail_no_description_available)
            } else {
                characterDescription.text = character.description
            }
        }
    }
}