package dev.amal.onthewakelivekmm.feature_queue.domain.repository

import dev.amal.onthewakelivekmm.core.domain.util.CommonFlow
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueResponse

interface QueueSocketService {
    suspend fun initSession(firstName: String): Resource<Unit>
    fun observeQueue(): CommonFlow<QueueResponse>
    suspend fun addToQueue(isLeftQueue: Boolean, firstName: String, timestamp: Long)
    suspend fun closeSession()
}