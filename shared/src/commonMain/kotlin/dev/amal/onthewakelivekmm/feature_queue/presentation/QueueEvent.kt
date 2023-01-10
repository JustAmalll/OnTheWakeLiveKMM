package dev.amal.onthewakelivekmm.feature_queue.presentation

sealed class QueueEvent {

    data class AddToQueue(
        val isLeftQueue: Boolean,
        val timestamp: Long
    ): QueueEvent()

    data class DeleteQueueItem(val queueItemId: String): QueueEvent()
    object OnQueueErrorSeen: QueueEvent()
}