package com.dicoding.storyapp.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityAuthBinding
import com.dicoding.storyapp.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private var _activityAuthBinding: ActivityAuthBinding? = null
    private val binding get() = _activityAuthBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val loginFragment = LoginFragment()
        val fragment = fragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)

        if (fragment !is LoginFragment) {
            fragmentManager.commit {
                add(R.id.container, loginFragment, LoginFragment::class.java.simpleName)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityAuthBinding = null
    }
}