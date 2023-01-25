package dev.amal.onthewakelivekmm.feature_queue.data.repository

import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.domain.util.CommonFlow
import dev.amal.onthewakelivekmm.core.domain.util.toCommonFlow
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_FIRST_NAME
import dev.amal.onthewakelivekmm.core.util.Constants.WS_BASE_URL
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.core.util.SimpleResource
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueResponse
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueSocketService
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class QueueSocketServiceImpl(
    private val client: HttpClient,
    private val preferenceManager: PreferenceManager
) : QueueSocketService {

    private var socket: WebSocketSession? = null

    override suspend fun initSession(): SimpleResource = try {
        socket = client.webSocketSession {
            url(urlString = WS_BASE_URL)
        }
        if (socket?.isActive == true) Resource.Success(Unit)
        else Resource.Error("Couldn't establish a connection")
    } catch (exception: Exception) {
        Resource.Error("An unknown error occurred")
    }

    override fun observeQueue(): CommonFlow<QueueResponse> {
        return socket!!
            .incoming
            .consumeAsFlow()
            .filterIsInstance<Frame.Text>()
            .mapNotNull { Json.decodeFromString<QueueResponse>(it.readText()) }
            .toCommonFlow()
    }

    override suspend fun addToQueue(
        isLeftQueue: Boolean, firstName: String?
    ): SimpleResource {
        return try {
            val name = firstName ?: (preferenceManager.getString(PREFS_FIRST_NAME)
                ?: return Resource.Error("Your account data is empty, try to re-login"))

            val timestamp = Clock.System.now().toEpochMilliseconds()

            socket?.send(Frame.Text("$isLeftQueue/$name/$timestamp"))
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Resource.Error("An unknown error occurred")
        }
    }

    override suspend fun closeSession() {
        socket?.close()
        socket = null
    }
}