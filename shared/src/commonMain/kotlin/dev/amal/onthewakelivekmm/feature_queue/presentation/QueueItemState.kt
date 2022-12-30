package dev.amal.onthewakelivekmm.feature_queue.presentation

import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

data class QueueItemState(
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