package com.martdev.android.mygallery.utils

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.dev.adnetworkm.CheckNetworkStatus
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.mygallery.MyGalleryApp
import com.martdev.android.mygallery.viewmodel.PhotoViewModel
import com.martdev.android.mygallery.viewmodel.VideoViewModel

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val application = (requireActivity().application as MyGalleryApp)
    val photoDataUseCase = application.photoUseCase
    val videoDataUseCase = application.videoUseCase

    return ViewModelFactory(photoDataUseCase, videoDataUseCase)
}

fun Fragment.checkNetworkState(): Boolean {
    var isNetworkConnected: Boolean = false
    CheckNetworkStatus.getNetworkLiveData(requireContext()).observe(this, Observer {
        isNetworkConnected = when(it) {
            true -> {
                it
            }
            false -> {
                Snackbar.make(this.requireView(), "Please check network connection", Snackbar.LENGTH_SHORT).show()
                it
            }
        }
    })
    return isNetworkConnected
}

fun SearchView.query(viewModel: ViewModel) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener  {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return if (query != null) {
                when(viewModel) {
                    is PhotoViewModel -> viewModel.search(query)
                    is VideoViewModel -> viewModel.search(query)
                }
                this@query.onActionViewCollapsed()
                true
            } else false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }

    })
}