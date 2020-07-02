package com.martdev.android.mygallery.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.adapter.PhotoDataAdapter
import com.martdev.android.mygallery.databinding.PhotoRecyclerViewBinding
import com.martdev.android.mygallery.utils.checkNetworkState
import com.martdev.android.mygallery.utils.getViewModelFactory
import com.martdev.android.mygallery.utils.query
import com.martdev.android.mygallery.viewmodel.PhotoViewModel

class PhotoListFragment : Fragment() {

    private lateinit var binding: PhotoRecyclerViewBinding
    private val viewModel: PhotoViewModel by viewModels { getViewModelFactory() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            (binding.photoRecyclerView.adapter as PhotoDataAdapter).setNetworkState(it)
        })
        viewModel.isNetworkAvailable = checkNetworkState()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.photo_recycler_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.photoVM = viewModel

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.photoRecyclerView.adapter = PhotoDataAdapter(viewModel)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.keyword_search, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.query(viewModel)

        return super.onCreateOptionsMenu(menu, inflater)
    }
}