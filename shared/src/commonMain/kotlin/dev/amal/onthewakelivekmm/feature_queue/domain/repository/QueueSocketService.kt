package dev.amal.onthewakelivekmm.feature_queue.domain.repository

import dev.amal.onthewakelivekmm.core.domain.util.CommonFlow
import dev.amal.onthewakelivekmm.core.util.SimpleResource
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueResponse

interface QueueSocketService {
    suspend fun initSession(): SimpleResource
    fun observeQueue(): CommonFlow<QueueResponse>

    suspend fun addToQueue(
        isLeftQueue: Boolean,
        firstName: String? = null
    ): SimpleResource

    suspend fun closeSession()
}