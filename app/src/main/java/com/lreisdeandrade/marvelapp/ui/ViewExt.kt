package com.lreisdeandrade.marvelapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.lreisdeandrade.marvellapp.R

fun ImageView.loadUrl(url: String) {
    Glide.with(context).applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.avengers_place_holder))
        .load(url).into(this)

}

fun View.showSnackBar(message: String, duration: Int, actionText: String, actionAction: () -> Unit) {
    Snackbar.make(this, message, duration)
        .setAction(actionText, { actionAction() })
        .show()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}