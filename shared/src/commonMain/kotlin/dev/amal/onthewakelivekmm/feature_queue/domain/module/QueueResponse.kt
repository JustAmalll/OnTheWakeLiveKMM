package dev.amal.onthewakelivekmm.feature_queue.domain.module

import kotlinx.serialization.Serializable

@Serializable
data class QueueResponse(
    val isDeleteAction: Boolean = false,
    val queueItem: QueueItem? = null,
    val error: String? = null
)