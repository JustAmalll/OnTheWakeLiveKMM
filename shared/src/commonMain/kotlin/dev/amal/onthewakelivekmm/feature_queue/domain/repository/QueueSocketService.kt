package dev.amal.onthewakelivekmm.feature_queue.domain.repository

import dev.amal.onthewakelivekmm.core.domain.util.CommonFlow
import dev.amal.onthewakelivekmm.core.util.SimpleResource
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueResponse

interface QueueSocketService {
    suspend fun initSession(): SimpleResource
    fun observeQueue(): CommonFlow<QueueResponse>
    fun canAddToQueue(isLeftQueue: Boolean, queue: List<QueueItem>): SimpleResource
    suspend fun addToQueue(isLeftQueue: Boolean, timestamp: Long): SimpleResource
    suspend fun closeSession()
}