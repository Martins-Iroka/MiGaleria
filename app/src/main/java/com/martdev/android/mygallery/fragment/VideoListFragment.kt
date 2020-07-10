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
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.martdev.android.mygallery.MyGalleryPagerFragmentDirections
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.adapter.OnClickListener
import com.martdev.android.mygallery.adapter.VideoDataAdapter
import com.martdev.android.mygallery.databinding.VideoRecyclerViewBinding
import com.martdev.android.mygallery.utils.*
import com.martdev.android.mygallery.viewmodel.VideoViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class VideoListFragment : Fragment(), AnkoLogger {

    private lateinit var binding: VideoRecyclerViewBinding
    private val viewModel: VideoViewModel by activityViewModels { getViewModelFactory() }

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var adapter: VideoDataAdapter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.isInternetAvailable = requireActivity().checkNetworkState()

        info {
            viewModel.isInternetAvailable
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.video_recycler_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.videoVM = viewModel

        swipeRefreshLayout = binding.swipe
        swipeRefreshLayout.refresh()
        observers()
        setupRecyclerView()
        setupSnackbar()
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
            binding.swipe.isRefreshing = it
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.searchKeyword.value?.let {
            viewModel.getData(it)
        } ?: viewModel.getData("nature")
    }

    override fun onResume() {
        super.onResume()
        info {
            "onResume called"
        }
    }

    private fun setupSnackbar() {
        binding.root.setupSnackbar(
            viewLifecycleOwner,
            viewModel.snackBarMessage,
            Snackbar.LENGTH_LONG
        )
    }

    private fun setupRecyclerView() {
        adapter = VideoDataAdapter(OnClickListener {
            val action = MyGalleryPagerFragmentDirections.actionMyGalleryPagerFragmentToVideoPlayerFragment(it)
            findNavController().navigate(action)
        }) {video ->
            viewModel.setFileUrl(video.video_files[2].link)
            if (viewModel.isInternetAvailable) viewModel.setFileName("${video.user.name}.mp4".trim())
        }
        binding.videoRecyclerView.adapter = adapter
    }

    private fun downloadFile(file: Uri) {
        val ktor = HttpClient(Android)
        context?.contentResolver?.openOutputStream(file)?.let {
            viewModel.downloadFile(it, ktor, file)
        }
        viewFile()
    }

    private fun viewFile() {
        viewModel.file.observe(viewLifecycleOwner, EventObserver {uri ->
            uri?.let { viewFileExt(it) }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DOWNLOAD_FILE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let {uri ->
                downloadFile(uri)
            }
        }
    }

    private fun SwipeRefreshLayout.refresh() {
        this.setOnRefreshListener {
            val keyword = viewModel.searchKeyword.value
            keyword?.let { viewModel.getData(it) }?: viewModel.getData("nature")
        }
    }
}