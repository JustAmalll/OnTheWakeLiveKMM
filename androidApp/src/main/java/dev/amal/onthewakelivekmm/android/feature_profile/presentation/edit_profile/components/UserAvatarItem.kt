package dev.amal.onthewakelivekmm.android.feature_profile.presentation.edit_profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import dev.amal.onthewakelivekmm.android.R
import dev.amal.onthewakelivekmm.android.core.utils.CropActivityResultContract

@ExperimentalMaterial3Api
@Composable
fun UserAvatarItem(
    imageLoader: ImageLoader,
    profilePictureUri: Uri,
    onAvatarCropped: (Uri?) -> Unit
) {
    val isImageLoading = remember { mutableStateOf(false) }

    val cropActivityLauncher = rememberLauncherForActivityResult(
        contract = CropActivityResultContract(16f, 16f)
    ) { onAvatarCropped(it) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { if (it != null) cropActivityLauncher.launch(it) }

    Card(
        modifier = Modifier
            .padding(top = 30.dp)
            .size(140.dp),
        shape = RoundedCornerShape(40.dp),
        onClick = {
            galleryLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        },
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (!isImageLoading.value) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Person,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = stringResource(
                        id = R.string.person_icon
                    )
                )
            }
            if (isImageLoading.value) CircularProgressIndicator(
                modifier = Modifier.size(42.dp)
            )
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberAsyncImagePainter(
                    model = profilePictureUri,
                    imageLoader = imageLoader,
                    onLoading = { isImageLoading.value = true },
                    onError = { isImageLoading.value = false },
                    onSuccess = { isImageLoading.value = false }
                ),
                contentDescription = stringResource(id = R.string.user_picture)
            )
        }
    }
}