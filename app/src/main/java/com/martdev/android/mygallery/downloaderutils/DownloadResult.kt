package com.martdev.android.mygallery.downloaderutils

sealed class DownloadResult {
    data class Success(val data: ByteArray) : DownloadResult() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Success

            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }

    data class Error(val message: String, val cause: Exception? = null) : DownloadResult()
    data class Progress(val progress: Int): DownloadResult()
}