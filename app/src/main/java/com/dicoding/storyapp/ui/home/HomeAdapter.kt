package com.dicoding.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.source.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ListStoryItemBinding
import com.dicoding.storyapp.ui.detail.DetailActivity

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    private var listStory = ArrayList<ListStoryItem>()

    fun setData(data: List<ListStoryItem>?) {
        if (data == null) return
        listStory.clear()
        listStory.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ListStoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = listStory[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    inner class ListViewHolder(var binding: ListStoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .centerCrop()
                .fitCenter()
                .placeholder(R.drawable.ic_custom_loading)
                .error(R.drawable.ic_custom_error)
                .into(binding.ivStoryPhoto)
            binding.tvStoryName.text = story.name

            itemView.setOnClickListener {
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    binding.ivStoryPhoto,
                    "story_photo"
                )

                val detailActivity = Intent(itemView.context, DetailActivity::class.java)
                detailActivity.putExtra(DetailActivity.EXTRA_DATA, story)
                itemView.context.startActivity(detailActivity, optionsCompat.toBundle())
            }
        }
    }
}
