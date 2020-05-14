package com.lreisdeandrade.marvelapp.util.ui.extension

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lreisdeandrade.marvelapp.ViewModelFactory

fun <T : ViewModel> Fragment.obtainViewModel(application: Application, viewModelClass: Class<T>) =
    ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(viewModelClass)