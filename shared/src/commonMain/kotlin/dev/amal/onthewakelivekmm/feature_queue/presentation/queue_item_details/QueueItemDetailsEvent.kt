package dev.amal.onthewakelivekmm.feature_queue.presentation.queue_item_details

sealed class QueueItemDetailsEvent {
    data class LoadQueueItemDetails(val queueItemId: String): QueueItemDetailsEvent()
    object OnErrorSeen: QueueItemDetailsEvent()
}