package com.martdev.android.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.martdev.android.data.toEntity
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.entity.PhotoDataEntity
import com.martdev.android.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhotoDataPageSource(
    private val query: String?,
    private val localDataSource: LocalDataSource<PhotoDataEntity>,
    private val remoteDataSource: RemoteDataSource<PhotoData>,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, PhotoData>() {

    var networkState = MutableLiveData<Result<List<PhotoData>>>()
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoData>
    ) {
        fetchData(params.requestedLoadSize, page = 1) {photoData ->
            callback.onResult(photoData, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoData>) {
        val page = params.key
        fetchData(params.requestedLoadSize, page = page) {photoData ->
            callback.onResult(photoData, page + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoData>) {
        TODO("Not yet implemented")
    }

    private fun fetchData(per_page: Int, page: Int, callback: (List<PhotoData>) -> Unit) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val result = if (query.isNullOrEmpty()) {
                    remoteDataSource.load(per_page, page)
                } else remoteDataSource.search(query, 15, page)

                when (result.status) {
                    Result.Status.LOADING -> networkState.value = Result.loading()
                    Result.Status.SUCCESS -> {
                        localDataSource.deleteData()
                        val data = result.data!!
                        networkState.postValue(Result.success(data))
                        data.forEach {
                            it.photos.forEach {photo ->
                                localDataSource.saveData(photo.toEntity())
                            }
                        }
                        callback(data)
                    }
                    Result.Status.ERROR -> {
                        networkState.postValue(Result.error(result.message))
                    }
                }
            }
        }
    }
}