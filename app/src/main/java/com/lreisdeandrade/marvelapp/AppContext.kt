package com.lreisdeandrade.marvelapp

import android.app.Application
import androidx.room.Room
import com.jakewharton.threetenabp.AndroidThreeTen
import com.lreisdeandrade.marvellapp.BuildConfig
import com.lreisdeandrade.marvelservice.LoggingInterceptor
import com.lreisdeandrade.marvelservice.MarvelApiEndPoint
import com.lreisdeandrade.marvelservice.MarvellModule
import com.lreisdeandrade.marvelservice.dao.CharacterDataBase
import timber.log.Timber

class AppContext : Application() {
    internal lateinit var database: CharacterDataBase

    companion object {
        lateinit var instance: AppContext private set

    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        initDagger()
        initializeTimber()
        initializeTimezone()
        initializeApiModules()
        initializeRoom()
    }

    private fun initDagger() {
//        DaggerInjector.initializeApplicationComponent(this);
    }

    private fun initializeTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initializeTimezone() {
        AndroidThreeTen.init(this)
    }

    private fun initializeApiModules() {
        MarvellModule.setRetrofit(MarvelApiEndPoint.DEVELOP, LoggingInterceptor.Level.FULL)
    }

    private fun initializeRoom() {
        database = Room
            .databaseBuilder(applicationContext, CharacterDataBase::class.java, "CharacterDataBase")
            .fallbackToDestructiveMigration()
            .build()
    }
}
