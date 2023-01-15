package dev.amal.onthewakelivekmm.feature_queue.presentation.queue_item_details

import dev.amal.onthewakelivekmm.feature_profile.domain.module.Profile

data class QueueItemDetailsState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val error: String? = null
)