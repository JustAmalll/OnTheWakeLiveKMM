package com.onthewake.onthewakelive.core.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class BasicApiResponse<T>(
    val successful: Boolean,
    val message: String? = null,
    val data: T? = null
)
