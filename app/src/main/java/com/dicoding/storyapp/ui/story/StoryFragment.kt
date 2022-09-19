package com.dicoding.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.source.local.UserPreferences
import com.dicoding.storyapp.databinding.FragmentStoryBinding
import com.dicoding.storyapp.ui.upload.UploadActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoryFragment : Fragment(), View.OnClickListener {

    private var _fragmentStoryBinding: FragmentStoryBinding? = null
    private val binding get() = _fragmentStoryBinding!!

    private val storyViewModel by viewModels<StoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentStoryBinding = FragmentStoryBinding.inflate(LayoutInflater.from(requireActivity()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreferences(requireContext())
        val token = pref.getToken()

        storyViewModel.getAllStories(token).observe(viewLifecycleOwner) { result ->
            val storyAdapter = StoryAdapter()
            binding.rvStoryList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            storyAdapter.submitData(lifecycle, result)
            binding.rvStoryList.adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }

        binding.fabUpload.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab_upload -> {
                val uploadActivity = Intent(requireActivity(), UploadActivity::class.java)
                startActivity(uploadActivity)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentStoryBinding = null
    }
}