package com.lreisdeandrade.marvellapp.features.favorite

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.lreisdeandrade.marvelapp.ui.favorite.FavoriteViewModel
import com.lreisdeandrade.marvelapp.util.scheduler.ImmediateSchedulerProvider
import com.lreisdeandrade.marvellapp.features.util.DummyData
import com.lreisdeandrade.marvelservice.dao.CharacterDao
import com.lreisdeandrade.marvelservice.dao.CharacterDataBase
import com.lreisdeandrade.marvelservice.model.Character
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class FavoriteViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var context: Application

    @Mock
    private lateinit var database: CharacterDataBase
    @Mock
    private lateinit var characterDao: CharacterDao

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var listFavoritesLiveMock: Observer<ArrayList<Character>>

    private lateinit var isLoadingLiveMock: Observer<Boolean>
    private lateinit var isFavoriteEmptyListLiveMock: Observer<Boolean>

    private lateinit var charactersMock: ArrayList<Character>
    private lateinit var charactersEmptyMock: ArrayList<Character>


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        setupContext()
        setupViewModelMocks()
        setupCharactersMock()

        favoriteViewModel = FavoriteViewModel(context, database, ImmediateSchedulerProvider())
    }

    private fun setupContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock(Resources::class.java))

    }

    private fun setupViewModelMocks() {

        isLoadingLiveMock = mock(Observer::class.java) as Observer<Boolean>
        listFavoritesLiveMock = mock(Observer::class.java) as Observer<ArrayList<Character>>
        isFavoriteEmptyListLiveMock = mock(Observer::class.java) as Observer<Boolean>
    }

    private fun setupCharactersMock() {
        charactersMock = DummyData.arrayListCharacter()
        charactersEmptyMock = arrayListOf()
    }

    @Test
    fun loadFavoriteList() {

        with(favoriteViewModel) {
            isLoadingLive.observeForever(isLoadingLiveMock)
            listFavoritesLive.observeForever(listFavoritesLiveMock)
            isEmptyListLive.observeForever(isFavoriteEmptyListLiveMock)


            `when`(database.characterDao()).thenReturn(characterDao)
            `when`(characterDao.getAll()).thenReturn(charactersMock)

            getAllFavoritesCharacters()
            isEmptyListLive(charactersMock)

            verify(isLoadingLiveMock).onChanged(true)
            verify(listFavoritesLiveMock).onChanged(charactersMock)
            verify(isLoadingLiveMock).onChanged(false)
            verify(isFavoriteEmptyListLiveMock).onChanged(false)
            verify(isFavoriteEmptyListLiveMock, never()).onChanged(true)

        }
    }

    @Test
    fun loadFavoriteListEmpty() {
        with(favoriteViewModel) {
            isLoadingLive.observeForever(isLoadingLiveMock)
            isEmptyListLive.observeForever(isFavoriteEmptyListLiveMock)
            listFavoritesLive.observeForever(listFavoritesLiveMock)

            `when`(database.characterDao()).thenReturn(characterDao)
            `when`(characterDao.getAll()).thenReturn(charactersEmptyMock)

            getAllFavoritesCharacters()
            isEmptyListLive(charactersEmptyMock)

            verify(isLoadingLiveMock).onChanged(true)
            verify(isLoadingLiveMock).onChanged(false)
            verify(isFavoriteEmptyListLiveMock).onChanged(true)
            verify(isFavoriteEmptyListLiveMock, never()).onChanged(false)

        }
    }
}