package com.martdev.android.mygallery.downloaderutils

//suspend fun HttpClient.downloadFile(url: String): Flow<DownloadResult> {
//    return flow {
//        try {
//            val response = call {
//                url(url)
//                method = HttpMethod.Get
//            }.response
//
//            if (response.status.isSuccess()) {
//                val data = ByteArray(response.contentLength()?.toInt()!!)
//                var offset = 0
//
//                do {
//                    val currentRead = response.content.readAvailable(data, offset, data.size)
//                    offset += currentRead
//                    val progress = (offset * 100f / data.size).roundToInt()
//                    emit(DownloadResult.Progress(progress))
//                } while (currentRead > 0)
//
//                response.close()
//                emit(DownloadResult.Success(data))
//            } else emit(DownloadResult.Error("Downloading failed"))
//        } catch (e: TimeoutCancellationException) {
//            emit(DownloadResult.Error("Connection timed out", e))
//        } catch (t: Throwable) {
//            emit(DownloadResult.Error("Failed to connect"))
//        }
//    }
//}