package com.dicoding.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.data.source.local.UserPreferences
import com.dicoding.storyapp.databinding.ActivitySplashBinding
import com.dicoding.storyapp.ui.auth.AuthActivity
import com.dicoding.storyapp.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var _activitySplashBinding: ActivitySplashBinding? = null
    private val binding get() = _activitySplashBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences(this)
        val token = pref.getToken()

        Handler(Looper.getMainLooper()).postDelayed({
            if (token.isNullOrEmpty()) {
                val authActivity = Intent(this, AuthActivity::class.java)
                startActivity(authActivity)
                finish()
            } else {
                val homeActivity = Intent(this, HomeActivity::class.java)
                startActivity(homeActivity)
                finish()
            }
        }, DELAY_MILLIS)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activitySplashBinding = null
    }

    private companion object {
        private const val DELAY_MILLIS = 3000L
    }
}