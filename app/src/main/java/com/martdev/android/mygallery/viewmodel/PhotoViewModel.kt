package com.martdev.android.mygallery.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.martdev.android.data.usecase.UseCase
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo
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
import timber.log.Timber
import java.io.OutputStream

class PhotoViewModel(private val photoUseCase: UseCase<Photo>) : BaseViewModel<Photo>(),
    AnkoLogger {

    override val searchKeyword: MutableLiveData<String> = MutableLiveData()

    override val data: MutableLiveData<List<Photo>> = MutableLiveData()

    override val snackBarMessage: MutableLiveData<Event<Int>> = MutableLiveData()

    override val networkState: MutableLiveData<Result<List<Photo>>> = MutableLiveData()

    val state: LiveData<Result<List<Photo>>> = networkState

    val loading = MutableLiveData<Boolean>()

    private val _downloadProgress = MutableLiveData<Int>()
    val downloadProgress: LiveData<Int> = _downloadProgress

    private val fileUri = MutableLiveData<Event<Uri?>>()
    val file: LiveData<Event<Uri?>> = fileUri

    private val _fileName = MutableLiveData<Event<String>>()
    val fileName: LiveData<Event<String>> = _fileName

    private var fileURL = ""

    override fun getData(query: String) {
        if (query.isNotEmpty()) searchKeyword.value = query
        loading.value = true
        viewModelScope.launch {
            val result = photoUseCase(query, isInternetAvailable)
            when (result.status) {
                Result.Status.SUCCESS -> {
                    loading.value = false
                    val photos = result.data!!
                    data.value = photos
                    networkState.value = Result.success(photos)
                    if (result.data?.isEmpty()!!) {
                        snackBarMessage.value = Event(R.string.no_photos_to_display)
                    }
                }
                Result.Status.ERROR -> {
                    loading.value = false
                    networkState.value = Result.error("")
                    snackBarMessage.value = Event(R.string.cant_load_data)
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
                                fileUri.postValue(Event(uri))
                            }

                            is DownloadResult.Error -> {
                                _downloadProgress.value = 0
                                fileUri.postValue(Event(null))
                                snackBarMessage.postValue(Event(R.string.download_failed))
                                error {
                                    "Error found while downloading"
                                }
                            }

                            is DownloadResult.Progress -> _downloadProgress.postValue(it.progress)
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