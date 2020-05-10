package com.lreisdeandrade.marvelapp.ui.home

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.lreisdeandrade.marvellapp.R
import com.lreisdeandrade.marvelapp.ui.replaceFragmentInActivity
import com.lreisdeandrade.marvelapp.ui.setupActionBar
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(false)
        }

        bottomNavigation.setOnNavigationItemSelectedListener {
            changeFragment(it.itemId)
            return@setOnNavigationItemSelectedListener true
        }
        openHomeFragment()
    }

    private fun openHomeFragment() {
        val homeId = "home"
        var fragment: HomeFragment? =
            supportFragmentManager.findFragmentByTag(homeId) as HomeFragment?
        if (fragment == null) {
            // Create the fragment
            fragment = HomeFragment.newInstance()
            replaceFragmentInActivity(fragment, R.id.contentFrame, homeId)
        }
    }

    private fun openFavoriteFragment() {
        val favoriteId = "favoriteId"
        var fragment: FavoriteFragment? = supportFragmentManager.findFragmentByTag(favoriteId) as FavoriteFragment?
        if (fragment == null) {
            // Create the fragment
            fragment =
                FavoriteFragment.newInstance()
            replaceFragmentInActivity(fragment, R.id.contentFrame, favoriteId)
        }
//        FavoritePresenter(AppContext.instance.database, fragment, SchedulerProvider)
    }

    private fun changeFragment(@IdRes itemId: Int) {
        when (itemId) {
            R.id.action_home -> openHomeFragment()
            R.id.action_favorites -> openFavoriteFragment()
        }
    }

//    @VisibleForTesting
//    fun getCountingIdlingResource(): IdlingResource {
//        return EspressoIdlingResource.idlingResource
//    }
}
