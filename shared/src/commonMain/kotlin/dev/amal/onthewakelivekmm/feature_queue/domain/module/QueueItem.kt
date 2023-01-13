package dev.amal.onthewakelivekmm.feature_queue.domain.module

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