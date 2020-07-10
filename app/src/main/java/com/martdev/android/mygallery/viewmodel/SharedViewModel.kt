package com.martdev.android.mygallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.martdev.android.mygallery.adapter.PHOTO_PAGE_INDEX
import com.martdev.android.mygallery.adapter.VIDEO_PAGE_INDEX
import com.martdev.android.mygallery.utils.Event

class SharedViewModel : ViewModel() {

    private val _searchPhoto = MutableLiveData<Event<String>>()
    val searchPhoto: LiveData<Event<String>> = _searchPhoto

    private val _searchVideo = MutableLiveData<Event<String>>()
    val searchVideo: LiveData<Event<String>> = _searchVideo

    fun search(currentTab: Int, query: String) {
        when(currentTab) {
            PHOTO_PAGE_INDEX -> {_searchPhoto.value = Event(query)}
            VIDEO_PAGE_INDEX -> _searchVideo.value = Event(query)
        }
    }
}