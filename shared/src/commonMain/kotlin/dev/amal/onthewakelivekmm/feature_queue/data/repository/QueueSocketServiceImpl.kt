package dev.amal.onthewakelivekmm.feature_queue.data.repository

import dev.amal.onthewakelivekmm.core.domain.util.CommonFlow
import dev.amal.onthewakelivekmm.core.domain.util.toCommonFlow
import dev.amal.onthewakelivekmm.core.util.Constants.WS_BASE_URL
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueResponse
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueSocketService
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class QueueSocketServiceImpl(
    private val client: HttpClient
) : QueueSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(
        firstName: String
    ): Resource<Unit> = try {
        socket = client.webSocketSession {
            url(urlString = "$WS_BASE_URL?firstName=$firstName")
        }
        if (socket?.isActive == true) Resource.Success(Unit)
        else Resource.Error("Couldn't establish a connection")
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown error occurred")
    }

    override fun observeQueue(): CommonFlow<QueueResponse> {
        return socket!!
            .incoming
            .consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .mapNotNull {
                val queueResponse = Json.decodeFromString<QueueResponse>(it.readText())
                QueueResponse(
                    isDeleteAction = queueResponse.isDeleteAction,
                    queueItem = queueResponse.queueItem
                )
            }
            .toCommonFlow()
    }

    override suspend fun addToQueue(
        isLeftQueue: Boolean,
        firstName: String,
        timestamp: Long
    ) {
        socket?.send(Frame.Text("$isLeftQueue/$firstName/$timestamp"))
    }

    override suspend fun closeSession() {
        socket?.close()
        socket = null
    }
}