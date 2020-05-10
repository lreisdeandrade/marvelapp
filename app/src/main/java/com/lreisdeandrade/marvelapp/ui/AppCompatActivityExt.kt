package com.lreisdeandrade.marvelapp.ui

import android.app.Activity
import android.app.Application
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.lreisdeandrade.marvelapp.ViewModelFactory
import timber.log.Timber

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int, tag: String) {
    supportFragmentManager.transact {
        replace(frameId, fragment, tag)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun Activity.requiredBundleNotFound(bundleNotFoundName: String) {
    Timber.e("Bundle %s is required and was not found", bundleNotFoundName)
    finish()
}

fun <T : ViewModel> AppCompatActivity.obtainViewModel(application: Application, viewModelClass: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}