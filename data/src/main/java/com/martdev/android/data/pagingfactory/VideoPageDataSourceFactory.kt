package com.martdev.android.data.pagingfactory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.martdev.android.data.paging.VideoDataPageSource
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope

class VideoPageDataSourceFactory(
    private val query: String?,
    private val localDataSource: LocalDataSource<VideoDataEntity>,
    private val remoteDataSource: RemoteDataSource<VideoData>,
    private val scope: CoroutineScope
) : DataSource.Factory<Int, Video>(){

    val liveData = MutableLiveData<VideoDataPageSource>()

    override fun create(): DataSource<Int, Video> {
        val source = VideoDataPageSource(query, localDataSource, remoteDataSource, scope)
        liveData.postValue(source)
        return source
    }
}