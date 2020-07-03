package com.martdev.android.mygallery.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
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

    private var isConnected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.photo_recycler_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.photoVM = viewModel

        isConnected = requireActivity().checkNetworkState()

        setupRecyclerView()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.i(PhotoListFragment::class.java.simpleName, "onStart in PhotoListFragment")
    }

    private fun setupRecyclerView() {
        binding.photoRecyclerView.adapter = PhotoDataAdapter(viewModel)

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            (binding.photoRecyclerView.adapter as PhotoDataAdapter).setNetworkState(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.keyword_search, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.query(viewModel, isConnected)

        return super.onCreateOptionsMenu(menu, inflater)
    }
}