package dev.amal.onthewakelivekmm.android.feature_profile.domain.repository

import android.net.Uri

interface FirebaseStorageRepository {

    suspend fun uploadUserAvatarToFirebaseStorage(
        profilePictureUri: Uri, userId: String?
    ): Uri?
}