package dev.amal.onthewakelivekmm.feature_queue.presentation.queue

sealed class QueueEvent {
    object InitSession : QueueEvent()
    data class AddToQueue(val isLeftQueue: Boolean, val firstName: String? = null) : QueueEvent()
    data class DeleteQueueItem(val queueItemId: String) : QueueEvent()
    object OnQueueErrorSeen : QueueEvent()
    object CloseSession : QueueEvent()
}