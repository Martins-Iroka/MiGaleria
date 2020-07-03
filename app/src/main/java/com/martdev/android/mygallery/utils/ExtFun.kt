package com.martdev.android.mygallery.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.dev.adnetworkm.CheckNetworkStatus
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.mygallery.MyGalleryApp
import com.martdev.android.mygallery.viewmodel.PhotoViewModel
import com.martdev.android.mygallery.viewmodel.VideoViewModel

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val application = (requireContext().applicationContext as MyGalleryApp)
    val photoDataUseCase = application.photoUseCase
    val videoDataUseCase = application.videoUseCase

    return ViewModelFactory(photoDataUseCase, videoDataUseCase)
}

fun FragmentActivity.checkNetworkState(): Boolean {
    var isNetworkConnected: Boolean = false
    CheckNetworkStatus.getNetworkLiveData(applicationContext).observe(this, Observer {
        isNetworkConnected = when(it) {
            true -> {
                it
            }
            false -> {
                Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show()
                it
            }
        }
    })
    return isNetworkConnected
}

fun SearchView.query(viewModel: ViewModel, isNetwork: Boolean) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener  {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return if (query != null) {
                when(viewModel) {
                    is PhotoViewModel -> {
                        viewModel.isConnected = isNetwork
                        viewModel.search(query)}
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