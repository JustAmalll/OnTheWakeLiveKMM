package dev.amal.onthewakelivekmm.feature_queue.data.repository

import dev.amal.onthewakelivekmm.core.util.Constants.BASE_URL
import dev.amal.onthewakelivekmm.feature_queue.data.remote.dto.QueueItemDto
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class QueueServiceImpl(
    private val client: HttpClient
) : QueueService {

    override suspend fun getQueue(): List<QueueItem> = try {
        val httpResponse = client.get("$BASE_URL/queue")
        println("http response is $httpResponse")
        httpResponse.body<List<QueueItemDto>>().map { it.toQueueItem() }
    } catch (e: Exception) {
        println("exception is $e")
        emptyList()
    }
}