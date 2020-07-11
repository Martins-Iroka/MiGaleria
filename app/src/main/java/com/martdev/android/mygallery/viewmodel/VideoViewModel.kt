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
import java.io.OutputStream

class VideoViewModel(private val videoUseCase: UseCase<Video>) : BaseViewModel<Video>() {

    private var bytes: ByteArray? = null

    override val searchKeyword: MutableLiveData<String> = MutableLiveData()

    override val data: MutableLiveData<List<Video>> = MutableLiveData()

    override val snackBarMessage: MutableLiveData<Event<Any>> = MutableLiveData()

    private val _loading = MutableLiveData<Event<Boolean>>()
    override val loading: LiveData<Event<Boolean>>
        get() = _loading

    private val _downloadProgress = MutableLiveData<Event<Int>>()
    override val downloadProgress: LiveData<Event<Int>> = _downloadProgress

    private val _fileUri = MutableLiveData<Event<Uri>>()
    override val fileUri: LiveData<Event<Uri>>
        get() = _fileUri

    private val _fileName = MutableLiveData<Event<String>>()
    override val fileName: LiveData<Event<String>> = _fileName

    private val _byteArray = MutableLiveData<Event<ByteArray>>()
    override val byteArray: LiveData<Event<ByteArray>>
        get() = _byteArray

    override fun getData(query: String) {
        searchKeyword.value = query
        _loading.value = Event(true)
        viewModelScope.launch {
            val result = videoUseCase(query, isInternetAvailable)

            when (result.status) {
                Result.Status.SUCCESS -> {
                    _loading.value = Event(false)
                    val videos = result.data!!
                    data.postValue(videos)
                    if (result.data?.isEmpty()!!) {
                        snackBarMessage.value = Event(R.string.no_videos_to_display)
                    }
                }
                Result.Status.ERROR -> {
                    _loading.value = Event(false)
                    snackBarMessage.postValue(Event(R.string.cant_load_data))
                }
            }
        }
    }

    private fun downloadFile(ktor: HttpClient, fileUrl: String, fileName: String) {
        if (isInternetAvailable) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    ktor.downloadFile(fileUrl).collect {
                        when (it) {
                            is DownloadResult.Success -> {
                                _downloadProgress.postValue(Event(0))
                                setFileName(fileName)
                                bytes = it.data
                            }

                            is DownloadResult.Error -> {
                                snackBarMessage.postValue(Event(it.message))
                                error {
                                    it.message
                                }
                            }

                            is DownloadResult.Progress -> _downloadProgress.postValue(Event(it.progress))
                        }
                    }
                }
            }
        } else snackBarMessage.value = Event(R.string.no_internet)
    }

    fun setBytesForWrite() {
        _byteArray.postValue(Event(bytes!!))
    }
    private fun setFileName(name: String) {
        _fileName.postValue(Event(name))
    }

    fun setFileUrl(video: Video, ktor: HttpClient) {
        val fileURL = video.video_files[0].link
        val fileName = "${video.user.name}.mp4".trim()

        downloadFile(ktor, fileURL, fileName)
    }

    fun writeByteToOutputStream(outputStream: OutputStream, byteArray: ByteArray, uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                outputStream.write(byteArray)
                _fileUri.postValue(Event(uri))
            }
        }
    }
}