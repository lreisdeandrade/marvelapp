package com.lreisdeandrade.marvelapp.ui.home

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.lreisdeandrade.marvelapp.ui.character.CharacterFragment
import com.lreisdeandrade.marvelapp.ui.favorite.FavoriteFragment
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelapp.util.ui.extension.replaceFragmentInActivity
import com.lreisdeandrade.marvelapp.util.ui.extension.setupActionBar
import com.lreisdeandrade.marvelapp.util.Constants.CHARACTER_FAVORITE_ID
import com.lreisdeandrade.marvelapp.util.Constants.CHARACTER_FRAMENT_ID
import com.lreisdeandrade.marvelapp.util.Constants.TAB_SELECTED
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private var currentSelectItemId = R.id.action_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (savedInstanceState != null) {
            currentSelectItemId = savedInstanceState.getInt(TAB_SELECTED)
            changeFragment(currentSelectItemId)
        } else {
            openHomeFragment()
        }

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(false)
        }

        bottomNavigation.setOnNavigationItemSelectedListener {
            changeFragment(it.itemId)
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun openHomeFragment() {
        val homeId = CHARACTER_FRAMENT_ID
        currentSelectItemId = R.id.action_home
        var fragment: CharacterFragment? =
            supportFragmentManager.findFragmentByTag(homeId) as CharacterFragment?
        if (fragment == null) {
            fragment = CharacterFragment.newInstance()
            replaceFragmentInActivity(fragment, R.id.contentFrame, homeId)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_SELECTED, currentSelectItemId)
    }

    private fun openFavoriteFragment() {
        val favoriteId = CHARACTER_FAVORITE_ID
        currentSelectItemId = R.id.action_favorites
        var fragment: FavoriteFragment? =
            supportFragmentManager.findFragmentByTag(favoriteId) as FavoriteFragment?
        if (fragment == null) {
            // Create the fragment
            fragment =
                FavoriteFragment.newInstance()
            replaceFragmentInActivity(fragment, R.id.contentFrame, favoriteId)
        }
    }

    private fun changeFragment(@IdRes itemId: Int) {
        when (itemId) {
            R.id.action_home -> openHomeFragment()
            R.id.action_favorites -> openFavoriteFragment()
        }
    }
}
