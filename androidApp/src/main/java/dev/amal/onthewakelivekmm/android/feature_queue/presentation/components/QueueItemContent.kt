package dev.amal.onthewakelivekmm.android.feature_queue.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardImageView
import dev.amal.onthewakelivekmm.feature_queue.domain.module.QueueItem

@ExperimentalAnimationApi
@Composable
fun QueueItemContent(
    queueItem: QueueItem,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onUserAvatarClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailsClicked(queueItem.id) }
            .clip(shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StandardImageView(
            imageLoader = imageLoader,
            model = queueItem.profilePictureUri,
            onUserAvatarClicked = onUserAvatarClicked
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = queueItem.firstName,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = queueItem.lastName,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}