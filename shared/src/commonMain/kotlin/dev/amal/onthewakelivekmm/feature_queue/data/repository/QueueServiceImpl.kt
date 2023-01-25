package dev.amal.onthewakelivekmm.feature_queue.data.repository

import com.onthewake.onthewakelive.core.data.dto.response.BasicApiResponse
import dev.amal.onthewakelivekmm.core.util.Constants.BASE_URL
import dev.amal.onthewakelivekmm.core.util.Resource
import dev.amal.onthewakelivekmm.core.util.SimpleResource
import dev.amal.onthewakelivekmm.feature_profile.data.remote.response.ProfileResponse
import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile
import dev.amal.onthewakelivekmm.feature_queue.data.remote.dto.QueueItemDto
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem
import dev.amal.onthewakelivekmm.feature_queue.domain.repository.QueueService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*

class QueueServiceImpl(
    private val client: HttpClient
) : QueueService {

    override suspend fun getQueue(): List<QueueItem> = try {
        val httpResponse = client.get("$BASE_URL/queue")
        httpResponse.body<List<QueueItemDto>>().map { it.toQueueItem() }
    } catch (e: Exception) {
        emptyList()
    }

    override suspend fun getProfileDetails(queueItemId: String): Resource<Profile> {
        val result = try {
            client.get("$BASE_URL/queue/item/details") {
                parameter("queueItemId", queueItemId)
            }
        } catch (e: IOException) {
            return Resource.Error("Oops! Couldn't reach server. Check your internet connection.")
        } catch (exception: Exception) {
            return Resource.Error("Oops! Something went wrong. Please try again")
        }

        when (result.status.value) {
            in 200..299 -> Unit
            else -> return Resource.Error("Oops! Something went wrong. Please try again.")
        }

        return try {
            val response = result.body<BasicApiResponse<ProfileResponse>>()

            if (response.successful) {
                Resource.Success(response.data?.toProfile())
            } else {
                response.message?.let { msg ->
                    Resource.Error(msg)
                } ?: Resource.Error("Unknown Error")
            }
        } catch (exception: Exception) {
            Resource.Error("Oops! Something went wrong. Please try again.")
        }
    }

    override suspend fun deleteQueueItem(queueItemId: String): SimpleResource = try {
        val result = client.delete(urlString = "$BASE_URL/queue/item/delete") {
            parameter("queueItemId", queueItemId)
        }
        when (result.status.value) {
            in 200..299 -> Resource.Success(Unit)
            in 400..499 -> Resource.Error("")
            500 -> Resource.Error("")
            else -> Resource.Error("")
        }
    } catch (e: ClientRequestException) {
        Resource.Error("Oops! Something went wrong. Please try again")
    } catch (e: IOException) {
        Resource.Error("Oops! Couldn't reach server. Check your internet connection.")
    }
}