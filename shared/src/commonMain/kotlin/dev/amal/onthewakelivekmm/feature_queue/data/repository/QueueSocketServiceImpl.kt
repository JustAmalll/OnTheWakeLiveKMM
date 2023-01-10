package dev.amal.onthewakelivekmm.feature_queue.data.repository

import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.domain.util.CommonFlow
import dev.amal.onthewakelivekmm.core.domain.util.toCommonFlow
import dev.amal.onthewakelivekmm.core.util.Constants
import dev.amal.onthewakelivekmm.core.util.Constants.PREFS_FIRST_NAME
import dev.amal.onthewakelivekmm.core.util.Constants.WS_BASE_URL
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.core.util.SimpleResource
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem
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

    override fun canAddToQueue(
        isLeftQueue: Boolean, queue: List<QueueItem>
    ): SimpleResource {

        val userId = preferenceManager.getString(Constants.PREFS_USER_ID)
            ?: return Resource.Error("Oops! Your account data is empty, tru to re-login")

        val allQueue = queue.sortedWith(compareBy { it.timestamp })
        val leftQueueItems = allQueue.filter { it.isLeftQueue }
        val rightQueueItems = allQueue.filter { !it.isLeftQueue }

        val isUserAlreadyInQueue = allQueue.find { it.userId == userId } != null
        val userItemInLeftQueue = leftQueueItems.find { it.userId == userId }
        val userItemInRightQueue = rightQueueItems.find { it.userId == userId }

        val userPositionInLeftQueue = leftQueueItems.indexOf(userItemInLeftQueue)
        val userPositionInRightQueue = rightQueueItems.indexOf(userItemInRightQueue)

        return if (userId in Constants.ADMIN_IDS) Resource.Success(Unit)
        else if (!isUserAlreadyInQueue) Resource.Success(Unit)
        else if (isLeftQueue && userItemInLeftQueue != null) {
            Resource.Error("Oops! You are already in queue!")
        } else if (!isLeftQueue && userItemInRightQueue != null) {
            Resource.Error("Oops! You are already in queue!")
        } else if (isLeftQueue && userItemInRightQueue != null) {
            if (leftQueueItems.size - userPositionInRightQueue >= 4) Resource.Success(Unit)
            else if (userPositionInRightQueue - leftQueueItems.size >= 4) Resource.Success(Unit)
            else Resource.Error("Oops! You need at least 4 people in front of you!")
        } else if (!isLeftQueue && userItemInLeftQueue != null) {
            if (rightQueueItems.size - userPositionInLeftQueue >= 4) Resource.Success(Unit)
            else if (userPositionInLeftQueue - rightQueueItems.size >= 4) Resource.Success(Unit)
            else Resource.Error("Oops! You need at least 4 people in front of you!")
        } else Resource.Error("")
    }

    override suspend fun addToQueue(
        isLeftQueue: Boolean, timestamp: Long
    ): SimpleResource {
        return try {
            val firstName = preferenceManager.getString(PREFS_FIRST_NAME)
                ?: return Resource.Error("Your account data is empty, try to re-login")

            socket?.send(Frame.Text("$isLeftQueue/$firstName/$timestamp"))
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