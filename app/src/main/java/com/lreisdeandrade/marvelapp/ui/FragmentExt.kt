package com.lreisdeandrade.marvelapp.ui

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.lreisdeandrade.marvelapp.ViewModelFactory

fun <T : ViewModel> Fragment.obtainViewModel(application: Application, viewModelClass: Class<T>) =
    ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)