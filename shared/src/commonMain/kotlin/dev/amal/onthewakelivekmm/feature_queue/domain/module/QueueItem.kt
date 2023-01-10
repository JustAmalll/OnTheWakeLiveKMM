package dev.amal.onthewakelivekmm.feature_queue.domain.module

import dev.amal.onthewakelivekmm.feature_queue.presentation.QueueItemState
import kotlinx.serialization.Serializable

@Serializable
data class QueueItem(
    val id: String,
    val userId: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUri: String,
    val isLeftQueue: Boolean,
    val timestamp: Long
)

fun QueueItem.toQueueItemState(): QueueItemState = QueueItemState(
    id = id,
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    profilePictureUri = profilePictureUri,
    isLeftQueue = isLeftQueue,
    timestamp = timestamp
)