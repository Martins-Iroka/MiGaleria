package com.martdev.android.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.martdev.android.data.toEntity
import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoDataPageSource(
    private val query: String?,
    private val localDataSource: LocalDataSource<VideoDataEntity>,
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
                val result
                        = if (query.isNullOrEmpty()) remoteDataSource.load(15, page)
                else remoteDataSource.search(query, 15, page)

                when(result.status) {
                    Result.Status.LOADING -> networkState.value = Result.loading()
                    Result.Status.SUCCESS -> {
                        localDataSource.deleteData()
                        val data = result.data?.videos!!
                        networkState.postValue(Result.success(data))
                        data.forEach {video ->
                                localDataSource.saveData(video.toEntity())
                            }
                        callback(data)
                    }
                    Result.Status.ERROR -> {
                        networkState.postValue(Result.error(result.message))
                        retry()
                    }
                }
            }
        }
    }
}