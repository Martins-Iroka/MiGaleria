package com.martdev.android.mygallery.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.adapter.VideoDataAdapter
import com.martdev.android.mygallery.databinding.VideoRecyclerViewBinding
import com.martdev.android.mygallery.utils.checkNetworkState
import com.martdev.android.mygallery.utils.getViewModelFactory
import com.martdev.android.mygallery.utils.query
import com.martdev.android.mygallery.viewmodel.VideoViewModel

class VideoListFragment : Fragment() {

    private lateinit var binding: VideoRecyclerViewBinding
    private val viewModel: VideoViewModel by viewModels { getViewModelFactory() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.isNetworkAvailable = checkNetworkState()
        setupRecyclerView()
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            (binding.videoRecyclerView.adapter as VideoDataAdapter).setNetworkState(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.video_recycler_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.videoVM = viewModel

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.videoRecyclerView.adapter = VideoDataAdapter(viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.keyword_search, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.query(viewModel)

        return super.onCreateOptionsMenu(menu, inflater)
    }
}