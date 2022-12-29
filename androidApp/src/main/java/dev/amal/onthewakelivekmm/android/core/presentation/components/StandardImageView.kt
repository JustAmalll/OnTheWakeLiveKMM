package dev.amal.onthewakelivekmm.android.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter

@ExperimentalAnimationApi
@Composable
fun StandardImageView(
    imageLoader: ImageLoader,
    model: String,
    onUserAvatarClicked: (String) -> Unit
) {
    var isImageLoading by remember { mutableStateOf(false) }

    IconButton(
        onClick = { onUserAvatarClicked(model) },
        modifier = Modifier.size(46.dp),
    ) {
        AnimatedContent(targetState = isImageLoading) { isLoading ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (!isLoading && model.isEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person Icon"
                    )
                }
                if (isLoading) CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    strokeWidth = 2.dp
                )
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                    painter = rememberAsyncImagePainter(
                        model = model,
                        imageLoader = imageLoader,
                        onLoading = { isImageLoading = true },
                        onError = { isImageLoading = false },
                        onSuccess = { isImageLoading = false }
                    ),
                    contentDescription = "User Picture"
                )
            }
        }
    }
}