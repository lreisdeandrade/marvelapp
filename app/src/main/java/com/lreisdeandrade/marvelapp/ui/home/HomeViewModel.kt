package com.lreisdeandrade.marvelapp.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lreisdeandrade.marvelapp.base.BaseViewModel
import com.lreisdeandrade.marvelapp.util.BaseSchedulerProvider
import com.lreisdeandrade.marvelservice.character.remote.CharacterDataSource
import com.lreisdeandrade.marvelservice.model.CharacterResponse
import timber.log.Timber

internal class HomeViewModel(application: Application,
                             private val characterDataSource: CharacterDataSource,
                             private val scheduler: BaseSchedulerProvider
)
    : BaseViewModel(application) {

    internal val isLoadingLive: MutableLiveData<Boolean> = MutableLiveData()
    internal val hasErrorLive: MutableLiveData<Boolean> = MutableLiveData()
    internal val characterLive: MutableLiveData<CharacterResponse> = MutableLiveData()

    fun start() {
        //UNUSED
    }

    fun loadCharactersList() {
        isLoadingLive.postValue(true)

        characterDataSource.fetchCharacterList()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .subscribe({
                isLoadingLive.postValue(false)
                characterLive.postValue(it)
            }, {
                isLoadingLive.postValue(false)
                hasErrorLive.postValue(true)

                Timber.e(it, "loadCharacters: %s", it.message)
            })
    }
}