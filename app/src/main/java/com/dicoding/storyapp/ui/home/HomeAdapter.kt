package com.dicoding.storyapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.storyapp.ui.maps.MapsFragment
import com.dicoding.storyapp.ui.story.StoryFragment

class HomeAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = StoryFragment()
            1 -> fragment = MapsFragment()
        }

        return fragment as Fragment
    }
}
