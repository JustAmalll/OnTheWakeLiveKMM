package dev.amal.onthewakelivekmm.feature_queue.presentation

sealed class QueueSocketEvent {
    data class AddToQueue(
        val isLeftQueue: Boolean,
        val firstName: String,
        val timestamp: Long
    ): QueueSocketEvent()

    data class DeleteQueueItem(val queueItemId: String): QueueSocketEvent()
}