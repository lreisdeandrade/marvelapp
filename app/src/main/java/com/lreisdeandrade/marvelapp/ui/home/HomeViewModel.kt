package com.lreisdeandrade.marvelapp.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lreisdeandrade.marvelapp.base.BaseViewModel
import com.lreisdeandrade.marvelapp.util.BaseSchedulerProvider
import com.lreisdeandrade.marvelservice.character.remote.CharacterDataSource
import com.lreisdeandrade.marvelservice.model.Character
import com.lreisdeandrade.marvelservice.model.CharacterResponse
import timber.log.Timber

internal class HomeViewModel(
    application: Application,
    private val characterDataSource: CharacterDataSource,
    private val scheduler: BaseSchedulerProvider
) : BaseViewModel(application) {

    internal val isLoadingLive: MutableLiveData<Boolean> = MutableLiveData()
    internal val fetchCharacterLive: MutableLiveData<CharacterResponse> = MutableLiveData()
    internal val isBottomLoadingLive: MutableLiveData<Boolean> = MutableLiveData()
    internal val characterSearchLive: MutableLiveData<ArrayList<Character>> = MutableLiveData()
    internal val hasErrorLive: MutableLiveData<Boolean> = MutableLiveData()
    internal val hasErrorNoDataLive: MutableLiveData<Boolean> = MutableLiveData()

    fun loadCharactersList(offset: Int) {
        when (offset) {
            0 -> isLoadingLive.postValue(true)
            else -> isBottomLoadingLive.postValue(true)
        }
        characterDataSource.fetchCharacterList(offset)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnTerminate {
                when (offset) {
                    0 -> isLoadingLive.postValue(false)
                    else -> isBottomLoadingLive.postValue(false)
                }
            }
            .subscribe({
                isLoadingLive.postValue(false)
                fetchCharacterLive.postValue(it)
            }, {
                isLoadingLive.postValue(false)
                when (offset) {
                    0 -> hasErrorNoDataLive.postValue(true)
                }
                hasErrorLive.postValue(true)
                Timber.e(it, "loadCharacters: %s", it.message)
            })
    }

    fun filterCharacter(textSearch: String, charactersList: ArrayList<Character>?) {

        val filteredList: ArrayList<Character> = ArrayList()
        charactersList.let { character ->
            character?.forEach {
                if (it.name.toLowerCase().contains(textSearch.toLowerCase())) {
                    filteredList.add(it)
                }
            }
        }
        if (textSearch.isNotEmpty()) {
            characterSearchLive.postValue(filteredList)
        } else {
            characterSearchLive.postValue(charactersList)
        }
    }
}