package com.lreisdeandrade.marvellapp.features.character

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.lreisdeandrade.marvelapp.ui.character.CharacterViewModel
import com.lreisdeandrade.marvelapp.util.scheduler.ImmediateSchedulerProvider
import com.lreisdeandrade.marvellapp.features.util.DummyData
import com.lreisdeandrade.marvellapp.features.util.parseObject
import com.lreisdeandrade.marvelservice.character.CharacterRepository
import com.lreisdeandrade.marvelservice.model.Character
import com.lreisdeandrade.marvelservice.model.CharacterResponse
import io.reactivex.Single
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.`when`

class CharacterViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @Mock
    private lateinit var context: Application

    @Mock
    private lateinit var characterRepository: CharacterRepository

    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var isLoadingLiveMock: Observer<Boolean>
    private lateinit var fetchCharacterLiveMock: Observer<CharacterResponse>
    private lateinit var isBottomLoadingLiveMock: Observer<Boolean>
    private lateinit var hasErrorLiveMock: Observer<Boolean>
    private lateinit var hasErrorNoDataLiveMock: Observer<Boolean>
    private lateinit var characterSearchLiveMock: Observer<ArrayList<Character>>

    private lateinit var charactersMock: CharacterResponse

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        setupContext()
        setupViewModelMocks()
        setupCharactersMock()

        characterViewModel = CharacterViewModel(context, characterRepository, ImmediateSchedulerProvider())
    }

    private fun setupContext() {
        `when`<Context>(context.applicationContext).thenReturn(context)
        `when`(context.resources).thenReturn(mock(Resources::class.java))

    }

    private fun setupViewModelMocks() {
        isLoadingLiveMock = mock(Observer::class.java) as Observer<Boolean>
        hasErrorLiveMock = mock(Observer::class.java) as Observer<Boolean>
        hasErrorNoDataLiveMock = mock(Observer::class.java) as Observer<Boolean>
        isBottomLoadingLiveMock = mock(Observer::class.java) as Observer<Boolean>
        fetchCharacterLiveMock = mock(Observer::class.java) as Observer<CharacterResponse>
        characterSearchLiveMock = mock(Observer::class.java) as Observer<ArrayList<Character>>
    }

    private fun setupCharactersMock() {
        charactersMock = parseObject("characterResponse.json", CharacterResponse::class.java)
    }

    @Test
    fun `load characters with first offset with success`() {
        with(characterViewModel) {
            hasErrorLive.observeForever(hasErrorLiveMock)
            isBottomLoadingLive.observeForever(isBottomLoadingLiveMock)
            isLoadingLive.observeForever(isLoadingLiveMock)
            fetchCharacterLive.observeForever(fetchCharacterLiveMock)
            hasErrorNoDataLive.observeForever(hasErrorNoDataLiveMock)

            `when`(characterRepository.fetchCharacterList(anyInt())).thenReturn(
                Single.just(charactersMock)
            )

            loadCharactersList(0)

            verify(isLoadingLiveMock).onChanged(true)
            verify(isBottomLoadingLiveMock, never()).onChanged(any())

            verify(hasErrorLiveMock).onChanged(false)
            verify(hasErrorNoDataLiveMock).onChanged(false)

            verify(fetchCharacterLiveMock).onChanged(charactersMock)

            verify(isLoadingLiveMock).onChanged(false)
        }
    }

    @Test
    fun `load characters without firts offset with success`() {
        with(characterViewModel) {
            isLoadingLive.observeForever(isLoadingLiveMock)
            isBottomLoadingLive.observeForever(isBottomLoadingLiveMock)
            fetchCharacterLive.observeForever(fetchCharacterLiveMock)
            hasErrorLive.observeForever(hasErrorLiveMock)
            hasErrorNoDataLive.observeForever(hasErrorNoDataLiveMock)

            `when`(characterRepository.fetchCharacterList(anyInt())).thenReturn(
                Single.just(charactersMock)
            )

            loadCharactersList(2)

            verify(isBottomLoadingLiveMock).onChanged(true)
            verify(isLoadingLiveMock, never()).onChanged(any())

            verify(hasErrorLiveMock).onChanged(false)
            verify(hasErrorNoDataLiveMock).onChanged(false)

            verify(fetchCharacterLiveMock).onChanged(charactersMock)

            verify(isBottomLoadingLiveMock).onChanged(false)
            verify(hasErrorLiveMock).onChanged(false)
            verify(hasErrorNoDataLiveMock).onChanged(false)
        }
    }

    @Test
    fun `load characters with first offset with error`() {
        with(characterViewModel) {
            hasErrorLive.observeForever(hasErrorLiveMock)
            isBottomLoadingLive.observeForever(isBottomLoadingLiveMock)
            isLoadingLive.observeForever(isLoadingLiveMock)
            fetchCharacterLive.observeForever(fetchCharacterLiveMock)
            hasErrorNoDataLive.observeForever(hasErrorNoDataLiveMock)

            `when`(characterRepository.fetchCharacterList(anyInt())).thenReturn(
                Single.error(RuntimeException(""))
            )

            loadCharactersList(0)

            verify(isLoadingLiveMock).onChanged(true)
            verify(isBottomLoadingLiveMock, never()).onChanged(any())

            verify(fetchCharacterLiveMock, never()).onChanged(any())

            verify(isLoadingLiveMock).onChanged(false)
            verify(hasErrorLiveMock).onChanged(true)
            verify(hasErrorNoDataLiveMock).onChanged(true)
        }
    }

    @Test
    fun `load characters whitout first offset with error`() {
        with(characterViewModel) {
            hasErrorLive.observeForever(hasErrorLiveMock)
            isBottomLoadingLive.observeForever(isBottomLoadingLiveMock)
            isLoadingLive.observeForever(isLoadingLiveMock)
            fetchCharacterLive.observeForever(fetchCharacterLiveMock)
            hasErrorNoDataLive.observeForever(hasErrorNoDataLiveMock)

            `when`(characterRepository.fetchCharacterList(anyInt())).thenReturn(
                Single.error(RuntimeException(""))
            )

            loadCharactersList(2)

            verify(isBottomLoadingLiveMock).onChanged(true)
            verify(isLoadingLiveMock, never()).onChanged(any())

            verify(hasErrorLiveMock).onChanged(false)
            verify(hasErrorNoDataLiveMock).onChanged(false)

            verify(fetchCharacterLiveMock, never()).onChanged(any())

            verify(isBottomLoadingLiveMock).onChanged(false)
            verify(hasErrorLiveMock).onChanged(true)
        }
    }

    @Test
    fun  `test filtered list with query`() {
        with(characterViewModel) {
            characterSearchLive.observeForever(characterSearchLiveMock)

            var originalList =  DummyData.arrayListCharacter()
            var query =  "s"
            filterCharacter(query, originalList)

            val filteredList: ArrayList<Character> = ArrayList()
            originalList.let { character ->
                character?.forEach {
                    if (it.name.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(it)
                    }
                }
            }

            if(query.isNotEmpty()){
                verify(characterSearchLiveMock).onChanged(filteredList)
                assertTrue(filteredList.size > 0)
            }
        }
    }

    @Test
    fun  `test filtered list with empty query`() {
        with(characterViewModel) {
            characterSearchLive.observeForever(characterSearchLiveMock)

            var originalList =  DummyData.arrayListCharacter()
            var query =  ""
            filterCharacter(query, originalList)

            val filteredList: ArrayList<Character> = ArrayList()
            originalList.let { character ->
                character?.forEach {
                    if (it.name.toLowerCase().contains(query.toLowerCase()) ) {
                        filteredList.add(it)
                    }
                }
            }

            if(query.isEmpty()){
                verify(characterSearchLiveMock).onChanged(originalList)
                assertTrue(originalList.isNotEmpty())
            }
        }
    }
}