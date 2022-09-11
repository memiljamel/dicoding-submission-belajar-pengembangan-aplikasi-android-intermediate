package com.dicoding.storyapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.storyapp.databinding.ActivitySplashBinding
import com.dicoding.storyapp.ui.auth.AuthActivity
import com.dicoding.storyapp.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var _activitySplashBinding: ActivitySplashBinding? = null
    private val binding get() = _activitySplashBinding!!

    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            lifecycleScope.launch {
                splashViewModel.getBearerToken().collect { token ->
                    if (token.isNullOrEmpty()) {
                        val authActivity = Intent(this@SplashActivity, AuthActivity::class.java)
                        startActivity(authActivity)
                        finish()
                    } else {
                        val homeActivity = Intent(this@SplashActivity, HomeActivity::class.java)
                        startActivity(homeActivity)
                        finish()
                    }
                }
            }
        }, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activitySplashBinding = null
    }
}