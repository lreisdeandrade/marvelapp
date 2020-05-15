package com.lreisdeandrade.marvelapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.lreisdeandrade.marvelapp.ui.home.HomeActivity
import com.lreisdeandrade.marvelapp.util.ui.extension.loadGif
import com.lreisdeandrade.marvellapp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 8200L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        icSplash.loadGif()
        Handler().postDelayed(
            {
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT)
    }
}