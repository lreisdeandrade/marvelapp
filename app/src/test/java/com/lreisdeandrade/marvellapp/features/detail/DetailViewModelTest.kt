package com.lreisdeandrade.marvellapp.features.detail

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.lreisdeandrade.marvelapp.ui.characterdetail.DetailViewModel
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

class DetailViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var context: Application

    @Mock
    private lateinit var database: CharacterDataBase
    @Mock
    private lateinit var characterDao: CharacterDao

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var isFavoriteLiveMock: Observer<Boolean>

    private lateinit var charactersMock: Character

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        setupContext()
        setupViewModelMocks()
        setupCharactersMock()

        detailViewModel = DetailViewModel(context, database, ImmediateSchedulerProvider())
    }

    private fun setupContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock(Resources::class.java))

    }

    private fun setupViewModelMocks() {
        isFavoriteLiveMock = mock(Observer::class.java) as Observer<Boolean>
        charactersMock = DummyData.getCharacter()
    }

    private fun setupCharactersMock() {
        charactersMock = DummyData.getCharacter()
    }

    @Test
    fun `check favorite character`() {

        with(detailViewModel) {
            isFavoriteLive.observeForever(isFavoriteLiveMock)

            `when`(database.characterDao()).thenReturn(characterDao)
            `when`(characterDao.findById(charactersMock.id)).thenReturn(charactersMock)

            checkFavoriteCharacter(charactersMock.id)

            verify(isFavoriteLiveMock, never()).onChanged(false)
            verify(isFavoriteLiveMock).onChanged(true)

        }
    }

    @Test
    fun `check unfavorite character`() {
        with(detailViewModel) {
            isFavoriteLive.observeForever(isFavoriteLiveMock)

            `when`(database.characterDao()).thenReturn(characterDao)
            `when`(characterDao.findById(charactersMock.id)).thenReturn(null)

            checkFavoriteCharacter(charactersMock.id)

            verify(isFavoriteLiveMock, never()).onChanged(true)
            verify(isFavoriteLiveMock).onChanged(false)

        }
    }
}