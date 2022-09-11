package com.dicoding.storyapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.source.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private var _activityDetailBinding: ActivityDetailBinding? = null
    private val binding get() = _activityDetailBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_DATA) as ListStoryItem

        Glide.with(this)
            .load(story.photoUrl)
            .centerCrop()
            .fitCenter()
            .placeholder(R.drawable.ic_custom_loading)
            .error(R.drawable.ic_custom_error)
            .into(binding.ivStoryPhoto)
        binding.tvStoryName.text = story.name
        binding.tvStoryDescription.text = story.description
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityDetailBinding = null
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}