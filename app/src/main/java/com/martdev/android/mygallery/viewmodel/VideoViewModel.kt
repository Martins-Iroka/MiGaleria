package com.martdev.android.mygallery.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.martdev.android.data.usecase.UseCase
import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.downloaderutils.DownloadResult
import com.martdev.android.mygallery.downloaderutils.downloadFile
import com.martdev.android.mygallery.utils.Event
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.io.OutputStream

class VideoViewModel(private val videoUseCase: UseCase<Video>) : BaseViewModel<Video>(),
    AnkoLogger {
    override val searchKeyword: MutableLiveData<String> = MutableLiveData()

    override val data: MutableLiveData<List<Video>> = MutableLiveData()

    override val snackBarMessage: MutableLiveData<Event<Int>> = MutableLiveData()

    override val networkState: MutableLiveData<Result<List<Video>>> = MutableLiveData()

    val state: LiveData<Result<List<Video>>> = networkState

    val loading = MutableLiveData<Boolean>()

    private val _downloadProgress = MutableLiveData<Int>()
    val downloadProgress: LiveData<Int> = _downloadProgress

    private val fileUri = MutableLiveData<Event<Uri?>>()
    val file: LiveData<Event<Uri?>> = fileUri

    private val _fileName = MutableLiveData<Event<String>>()
    val fileName: LiveData<Event<String>> = _fileName

    private var fileURL = ""

    override fun getData(query: String) {
        searchKeyword.value = query
        loading.value = true
        viewModelScope.launch {
            val result = videoUseCase(query, isInternetAvailable)

            when (result.status) {
                Result.Status.SUCCESS -> {
                    loading.value = false
                    val videos = result.data!!
                    data.postValue(videos)
                    networkState.value = Result.success(videos)
                    if (result.data?.isEmpty()!!) {
                        snackBarMessage.value = Event(R.string.no_videos_to_display)
                    }
                }
                Result.Status.ERROR -> {
                    loading.value = false
                    networkState.value = Result.error("")
                    snackBarMessage.postValue(Event(R.string.cant_load_data))
                }
            }
        }
    }

    fun downloadFile(outputStream: OutputStream, ktor: HttpClient, uri: Uri) {
        if (isInternetAvailable) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    ktor.downloadFile(outputStream, fileURL).collect {
                        when (it) {
                            is DownloadResult.Success -> {
                                _downloadProgress.value = 0
                                fileUri.value = Event(uri)
                            }

                            is DownloadResult.Error -> {
                                _downloadProgress.value = 0
                                fileUri.value = Event(null)
                                snackBarMessage.postValue(Event(R.string.download_failed))
                                error {
                                    "Error found while downloading"
                                }
                            }

                            is DownloadResult.Progress -> _downloadProgress.value = it.progress
                        }
                    }
                }
            }
        }
    }

    fun setFileName(name: String) {
        _fileName.value = Event(name)
    }

    fun setFileUrl(url: String) {
        fileURL = url
    }
}