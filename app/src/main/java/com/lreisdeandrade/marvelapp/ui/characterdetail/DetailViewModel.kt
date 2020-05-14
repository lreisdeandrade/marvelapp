package com.lreisdeandrade.marvelapp.ui.characterdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lreisdeandrade.marvelapp.base.BaseViewModel
import com.lreisdeandrade.marvelapp.util.scheduler.BaseSchedulerProvider
import com.lreisdeandrade.marvelservice.dao.CharacterDataBase
import com.lreisdeandrade.marvelservice.model.Character
import io.reactivex.Observable
import timber.log.Timber

internal class DetailViewModel(
    application: Application,
    private val database: CharacterDataBase,
    private val schedulerProvider: BaseSchedulerProvider
) : BaseViewModel(application) {

    internal val checkFavorite: MutableLiveData<Boolean> = MutableLiveData()
    internal val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    fun saveFavoriteCharacter(character: Character) {
        Observable.just(0)
            .map { database.characterDao().insert(character) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({isFavorite.postValue(true) }, {
                Timber.e(it, "saveFavoriteCharacter: %s", it.message)
//                view.saveFavoriteError()
            })

    }

    fun removeFavoriteCharacter(character: Character) {
        Observable.just(0)
            .map {database.characterDao().delete(character) }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({isFavorite.postValue(false) }, {
                Timber.e(it, "deleteFavoriteCharacter: %s", it.message)
//                view.saveFavoriteError()
            })
    }

    fun checkFavoriteCharacter(characterId: Int) {
        Observable.just(0)
            .map {database.characterDao().findById(characterId) != null }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({checkFavorite.postValue(it) }, {
                Timber.e(it, "checkFavoriteCharacter: %s", it.message)
//                view.saveFavoriteError()
            })
    }
}