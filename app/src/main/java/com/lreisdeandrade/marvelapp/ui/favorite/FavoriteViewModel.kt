package com.lreisdeandrade.marvelapp.ui.favorite

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lreisdeandrade.marvelapp.base.BaseViewModel
import com.lreisdeandrade.marvelapp.util.BaseSchedulerProvider
import com.lreisdeandrade.marvelservice.dao.CharacterDataBase
import com.lreisdeandrade.marvelservice.model.Character
import io.reactivex.Observable
import timber.log.Timber

internal class FavoriteViewModel(
    application: Application, private val database: CharacterDataBase,
    private val schedulerProvider: BaseSchedulerProvider
) : BaseViewModel(application) {

    internal val listFavoritesLive: MutableLiveData<ArrayList<Character>> = MutableLiveData()
    internal val isLoadingLive: MutableLiveData<Boolean> = MutableLiveData()
    internal val isEmptyListLive: MutableLiveData<Boolean> = MutableLiveData()

    fun getAllFavoritesCharacters() {
        Observable.just(0)
            .map { database.characterDao().getAll() }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ listFavoritesLive.postValue(it as ArrayList<Character>)}, {
                Timber.e(it, "listFavoriteCharacters: %s", it.message)
//                view.saveFavoriteError()
            })
    }

    fun isEmptyListLive(favoriteItems: ArrayList<Character>){
        Observable.just(0)
            .map {favoriteItems.isEmpty()}
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ isEmptyListLive.postValue(it)}, {
                Timber.e(it, "verifyEmptyList: %s", it.message)
//                view.saveFavoriteError()
            })
    }
}