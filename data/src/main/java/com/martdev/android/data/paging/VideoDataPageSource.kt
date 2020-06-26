package com.martdev.android.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.martdev.android.data.toEntity
import com.martdev.android.domain.Result
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
) : PageKeyedDataSource<Int, VideoData>() {

    var networkState = MutableLiveData<Result<List<VideoData>>>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, VideoData>
    ) {
        fetchData(1) {videoData ->
            callback.onResult(videoData, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, VideoData>) {
        val page = params.key
        fetchData(page) { videoData ->
            callback.onResult(videoData, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, VideoData>) {
        TODO("Not yet implemented")
    }

    private fun fetchData(page: Int, callback: (List<VideoData>) -> Unit) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val result
                        = if (query.isNullOrEmpty()) remoteDataSource.load(15, page)
                else remoteDataSource.search(query, 15, page)

                when(result.status) {
                    Result.Status.LOADING -> networkState.value = Result.loading()
                    Result.Status.SUCCESS -> {
                        localDataSource.deleteData()
                        val data = result.data!!
                        networkState.postValue(Result.success(data))
                        data.forEach {
                            it.videos.forEach {video ->
                                localDataSource.saveData(video.toEntity())
                            }
                        }
                        callback(data)
                    }
                    Result.Status.ERROR -> networkState.postValue(Result.error(result.message))
                }
            }
        }
    }
}