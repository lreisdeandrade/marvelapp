package com.lreisdeandrade.marvelapp.ui.characterdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lreisdeandrade.marvelapp.base.BaseViewModel
//import com.lreisdeandrade.marvelservice.dao.CharacterDataBase
import com.lreisdeandrade.marvelservice.model.Character

//internal class DetailViewModel(
//    application: Application,
//    internal val database: CharacterDataBase) : BaseViewModel(application) {
//    internal val checkFavorite: MutableLiveData<Boolean> = MutableLiveData()
//    internal val removeFavorite: MutableLiveData<Boolean> = MutableLiveData()
//    internal val insertFavorite: MutableLiveData<Boolean> = MutableLiveData()
//
//    fun start() {
//        //UNUSED
//    }

//    fun saveFavoriteCharacter(character: Character) {
//        database.characterDao().insert(character)
//        insertFavorite.postValue(true)
//    }
//
//    fun removeFavoriteCharacter(character: Character) {
//        database.characterDao().delete(character)
//        removeFavorite.postValue(true)
//    }
//
//    fun checkFavorite(characterId: Int) {
//        checkFavorite.postValue(database.characterDao().findById(characterId) != null)
//    }
//}