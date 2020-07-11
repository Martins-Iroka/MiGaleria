package com.martdev.android.mygallery.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.adapter.PhotoDataAdapter
import com.martdev.android.mygallery.databinding.PhotoRecyclerViewBinding
import com.martdev.android.mygallery.utils.*
import com.martdev.android.mygallery.viewmodel.PhotoViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

class PhotoListFragment : Fragment() {

    private lateinit var binding: PhotoRecyclerViewBinding
    private val viewModel: PhotoViewModel by activityViewModels { getViewModelFactory() }

    private lateinit var adapter: PhotoDataAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.isInternetAvailable = requireActivity().checkNetworkState()
        if (!viewModel.isInternetAvailable) viewModel.isInternetAvailable = true

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.photo_recycler_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.photoVM = viewModel
        swipeRefreshLayout = binding.swipe
        swipeRefreshLayout.refresh()

        getData()
        observers()
        setupSnackbar()
        setupRecyclerView()
        return binding.root
    }

    private fun getData() {
        viewModel.searchKeyword.value?.let {
            viewModel.getData(it)
        } ?: viewModel.getData()
    }

    private fun observers() {
        viewModel.fileName.observe(viewLifecycleOwner, EventObserver {
            setDownloadedFileName(it)
        })
        viewModel.downloadProgress.observe(viewLifecycleOwner, EventObserver { progress ->
            adapter.setProgress(progress)
        })

        viewModel.loading.observe(viewLifecycleOwner, EventObserver {
            swipeRefreshLayout.isRefreshing = it
        })

        viewModel.fileUri.observe(viewLifecycleOwner, EventObserver { uri ->
            viewFileExt(uri)
        })
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(
            viewLifecycleOwner,
            viewModel.snackBarMessage,
            Snackbar.LENGTH_LONG)
    }

    private fun setupRecyclerView() {
        val ktor = HttpClient(Android)
        adapter = PhotoDataAdapter { photo ->
            viewModel.setFileUrl(photo, ktor)
        }
        binding.photoRecyclerView.adapter = adapter
    }

    private fun writeToFile(file: Uri) {
        context?.contentResolver?.openOutputStream(file)?.let { outputStream ->
            viewModel.byteArray.observe(viewLifecycleOwner, EventObserver { byte ->
                viewModel.writeByteToOutputStream(outputStream, byte, file)
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DOWNLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                viewModel.setBytesForWrite()
                writeToFile(uri)
            }
        }
    }

    private fun SwipeRefreshLayout.refresh() {
        this.setOnRefreshListener {
            val keyword = viewModel.searchKeyword.value
            keyword?.let { viewModel.getData(it) } ?: viewModel.getData()
        }
    }
}