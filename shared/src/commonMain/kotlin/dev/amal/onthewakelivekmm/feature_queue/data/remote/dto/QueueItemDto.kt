package dev.amal.onthewakelivekmm.feature_queue.data.remote.dto

import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

@kotlinx.serialization.Serializable
data class QueueItemDto(
    val id: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUri: String,
    val isLeftQueue: Boolean,
    val timestamp: Long
) {
    fun toQueueItem(): QueueItem = QueueItem(
        id = id,
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        profilePictureUri = profilePictureUri,
        isLeftQueue = isLeftQueue,
        timestamp = timestamp
    )
}
