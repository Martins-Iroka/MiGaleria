package com.martdev.android.mygallery.viewmodel

import android.net.Uri
import androidx.lifecycle.*
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

/*
class PhotoViewModel(private val photoUseCase: UseCase<Photo>) : BaseViewModel<Photo>() {

    */
/*override val searchKeyword: MutableLiveData<String> = MutableLiveData()

    override val data: MutableLiveData<List<Photo>> = MutableLiveData()

    override val snackBarMessage: MutableLiveData<Event<Any>> = MutableLiveData()

    private val _loading = MutableLiveData<Event<Boolean>>()
    override val loading: LiveData<Event<Boolean>>
        get() = _loading

    private val _downloadProgress = MutableLiveData<Event<Int>>()
    override val downloadProgress: LiveData<Event<Int>> = _downloadProgress

    private val _fileUri = MutableLiveData<Event<Uri>>()
    override val fileUri: LiveData<Event<Uri>> = _fileUri

    private val _fileName = MutableLiveData<Event<String>>()
    override val fileName: LiveData<Event<String>> = _fileName

    private var bytes: ByteArray? = null

    private val _byteArray = MutableLiveData<Event<ByteArray>>()
    override val byteArray: LiveData<Event<ByteArray>>
        get() = _byteArray

    override fun getData(query: String) {
        if (query.isNotEmpty()) searchKeyword.value = query
        _loading.value = Event(true)
        viewModelScope.launch {
            val result = photoUseCase(query, isInternetAvailable)
            if (result.status == Result.Status.SUCCESS) {
                _loading.value = Event(false)
                val photos = result.data!!
                data.value = photos
                if (result.data?.isEmpty()!!) {
                    snackBarMessage.value = Event(R.string.no_photos_to_display)
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

    fun setFileUrl(photo: Photo, ktor: HttpClient) {
        val fileURL = photo.src.original
        val fileName = "${photo.photographer}.jpeg".trim()

        downloadFile(ktor, fileURL, fileName)
    }

    fun writeByteToOutputStream(outputStream: OutputStream, byteArray: ByteArray, uri: Uri) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                outputStream.write(byteArray)
                _fileUri.postValue(Event(uri))
            }
        }
    }*//*

}*/
