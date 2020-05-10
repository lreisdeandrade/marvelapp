package com.lreisdeandrade.marvelapp

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lreisdeandrade.marvelapp.ui.characterdetail.DetailViewModel
import com.lreisdeandrade.marvelapp.ui.home.HomeViewModel
import com.lreisdeandrade.marvelapp.util.SchedulerProvider

class ViewModelFactory private constructor(private val application: AppContext) :
    ViewModelProvider.NewInstanceFactory() {

//    init {
//        DaggerInjector.getApplicationComponent().inject(this)
//    }

    override fun <T /**/ : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {

                isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(
                        application,
                        Injection.provideCharacterRepository(),
                        SchedulerProvider
                    )
                }

                isAssignableFrom(DetailViewModel::class.java) -> {
                    DetailViewModel(application, application.database, SchedulerProvider)
                }

                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) = INSTANCE
            ?: synchronized(ViewModelFactory::class.java) {
                val viewModelFactory = INSTANCE
                    ?: ViewModelFactory(application as AppContext)
                        .also { INSTANCE = it }
                viewModelFactory
            }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
