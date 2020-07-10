package com.martdev.android.mygallery.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.adapter.PhotoDataAdapter
import com.martdev.android.mygallery.databinding.PhotoRecyclerViewBinding
import com.martdev.android.mygallery.utils.*
import com.martdev.android.mygallery.viewmodel.PhotoViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class PhotoListFragment : Fragment(), AnkoLogger {

    private lateinit var binding: PhotoRecyclerViewBinding
    private val viewModel: PhotoViewModel by activityViewModels { getViewModelFactory() }

    private lateinit var adapter: PhotoDataAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        info { requireActivity().checkNetworkState() }
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

        observers()
        setupSnackbar()
        setupRecyclerView()
        return binding.root
    }

    private fun observers() {
        viewModel.fileName.observe(viewLifecycleOwner, EventObserver {
            setDownloadSource(it)
        })
        viewModel.downloadProgress.observe(viewLifecycleOwner, Observer { progress ->
            adapter.setProgress(progress)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = it
        })

    }

    override fun onStart() {
        super.onStart()
        info { "OnStart called" }
        viewModel.searchKeyword.value?.let {
            viewModel.getData(it)
        } ?: viewModel.getData()
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(
            viewLifecycleOwner,
            viewModel.snackBarMessage,
            Snackbar.LENGTH_LONG)
    }

    private fun setupRecyclerView() {
        adapter = PhotoDataAdapter(viewModel) { photo ->
            viewModel.setFileUrl(photo.src.original)
            if (viewModel.isInternetAvailable) viewModel.setFileName("${photo.photographer}.jpeg".trim())
        }
        binding.photoRecyclerView.adapter = adapter
    }

    private fun downloadFile(file: Uri) {
        val ktor = HttpClient(Android)
        context?.contentResolver?.openOutputStream(file)?.let {
            viewModel.downloadFile(it, ktor, file)
        }
        viewFile()
    }

    private fun viewFile() {
        viewModel.file.observe(viewLifecycleOwner, EventObserver { uri ->
            uri?.let { viewFileExt(it) }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DOWNLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                downloadFile(uri)
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