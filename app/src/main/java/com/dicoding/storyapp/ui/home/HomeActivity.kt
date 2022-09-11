package com.dicoding.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.Result
import com.dicoding.storyapp.data.source.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityHomeBinding
import com.dicoding.storyapp.ui.auth.AuthActivity
import com.dicoding.storyapp.ui.upload.UploadActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private var _activityHomeBinding: ActivityHomeBinding? = null
    private val binding get() = _activityHomeBinding!!

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            homeViewModel.getBearerToken().collect { token ->
                homeViewModel.getAllStories(token).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            setStoryList(result.data.listStory)
                        }
                        is Result.Error -> {
                            binding.progressIndicator.visibility = View.GONE
                            setErrorMessage(result.error)
                        }
                    }
                }
            }
        }

        binding.fabUpload.setOnClickListener(this)
    }

    private fun setStoryList(listStory: List<ListStoryItem>?) {
        val homeAdapter = HomeAdapter()
        binding.rvStoryList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        homeAdapter.setData(listStory)
        binding.rvStoryList.adapter = homeAdapter
    }

    private fun setErrorMessage(message: String?) {
        binding.animation.visibility = View.VISIBLE
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.tvErrorMessage.text = message
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_upload -> {
                val uploadActivity = Intent(this@HomeActivity, UploadActivity::class.java)
                startActivity(uploadActivity)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                homeViewModel.removeBearerToken()

                val authActivity = Intent(this@HomeActivity, AuthActivity::class.java)
                startActivity(authActivity)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityHomeBinding = null
    }
}
