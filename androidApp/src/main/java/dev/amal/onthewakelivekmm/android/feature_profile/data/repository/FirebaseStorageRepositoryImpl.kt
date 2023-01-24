package dev.amal.onthewakelivekmm.android.feature_profile.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import dev.amal.onthewakelivekmm.android.feature_profile.domain.repository.FirebaseStorageRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseStorageRepositoryImpl(
    private val storage: FirebaseStorage
) : FirebaseStorageRepository {

    override suspend fun uploadUserAvatarToFirebaseStorage(
        profilePictureUri: Uri,
        userId: String?
    ): Uri? = suspendCoroutine { continuation ->

        if (userId == null || userId.isEmpty()) {
            continuation.resume(null)
            return@suspendCoroutine
        }

        val ref = storage.reference.child(userId)

        ref.putFile(profilePictureUri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { uri -> continuation.resume(uri) }
        }
    }
}