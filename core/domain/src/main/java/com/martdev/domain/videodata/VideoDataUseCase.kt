package com.martdev.domain.videodata

class VideoDataUseCase(
    val videoDataSource: VideoDataSource
) {

    fun getVideoPosts(limit: Int, offset: Int) = videoDataSource.getVideoPosts(limit, offset)

    fun postComment(postId: String, content: String) = videoDataSource.postComment(postId, content)

    fun getCommentsByPostId(postId: String) = videoDataSource.getCommentsByPostID(postId)
}