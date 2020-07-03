package com.martdev.android.data.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.martdev.android.data.toUserEntity
import com.martdev.android.data.toVideoEntity
import com.martdev.android.data.toVideoFilesEntity
import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.VideoDataSource
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.local.entity.VideoEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoDataPageSource(
    private val query: String?,
    private val localDataSource: LocalDataSource<VideoEntity, VideoDataEntity>,
    private val remoteDataSource: RemoteDataSource<VideoData>,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, Video>() {

    var networkState = MutableLiveData<Result<List<Video>>>()

    var retryCallback: RetryCallback? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Video>
    ) {
        fetchData(1, { videoData ->
            retryCallback = null
            callback.onResult(videoData, null, 2)
        }, {
            retryCallback = object : RetryCallback {
                override fun invoke() {
                    loadInitial(params, callback)
                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {
        val page = params.key
        fetchData(page, { videoData ->
            retryCallback = null
            callback.onResult(videoData, page + 1)
        }, {
            retryCallback = object : RetryCallback {
                override fun invoke() {
                    loadAfter(params, callback)
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Video>) {
        TODO("Not yet implemented")
    }

    private fun fetchData(page: Int, callback: (List<Video>) -> Unit, retry: () -> Unit) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val result = if (query.isNullOrEmpty()) remoteDataSource.load(15, page)
                else remoteDataSource.search(query, 15, page)

                when (result.status) {
                    Result.Status.LOADING -> networkState.value = Result.loading()
                    Result.Status.SUCCESS -> {
                        val data = result.data?.videos!!
                        val source = localDataSource as VideoDataSource
                        source.deleteData()
                        networkState.postValue(Result.success(data))
                        Log.d(VideoDataPageSource::class.java.simpleName, data.toString())
                        data.forEach { video ->
                            source.saveData(video.toVideoEntity())
                            source.saveUser(video.user.toUserEntity(video.id))
                            source.saveVideoFile(video.video_files.toVideoFilesEntity(video.id))
                        }
                        callback(data)
                    }
                    Result.Status.ERROR -> {
                        networkState.postValue(Result.error(result.message))
                        Log.d(VideoDataPageSource::class.java.simpleName, result.message)
                        retry()
                    }
                }
            }
        }
    }
}