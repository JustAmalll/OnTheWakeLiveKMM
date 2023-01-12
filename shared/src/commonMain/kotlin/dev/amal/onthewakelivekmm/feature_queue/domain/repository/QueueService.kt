package dev.amal.onthewakelivekmm.feature_queue.domain.repository

import dev.amal.onthewakelivekmm.core.util.SimpleResource
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

interface QueueService {
    suspend fun getQueue(): List<QueueItem>
    suspend fun deleteQueueItem(queueItemId: String): SimpleResource
}