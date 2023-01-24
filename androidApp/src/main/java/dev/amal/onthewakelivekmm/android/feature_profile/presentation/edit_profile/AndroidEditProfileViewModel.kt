package dev.amal.onthewakelivekmm.android.feature_profile.presentation.edit_profile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.amal.onthewakelivekmm.android.feature_profile.domain.repository.FirebaseStorageRepository
import dev.amal.onthewakelivekmm.feature_auth.domain.use_case.ValidationUseCase
import dev.amal.onthewakelivekmm.feature_profile.domain.repository.ProfileRepository
import dev.amal.onthewakelivekmm.feature_profile.presentation.edit_profile.EditProfileEvent
import dev.amal.onthewakelivekmm.feature_profile.presentation.edit_profile.EditProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AndroidEditProfileViewModel @Inject constructor(
    private val validationUseCase: ValidationUseCase,
    private val profileRepository: ProfileRepository,
    private val firebaseStorageRepository: FirebaseStorageRepository
) : ViewModel() {

    private val viewModel by lazy {
        EditProfileViewModel(
            validationUseCase = validationUseCase,
            profileRepository = profileRepository,
            coroutineScope = viewModelScope
        )
    }

    init {
        onEvent(EditProfileEvent.GetProfile)
    }

    val state = viewModel.state

    private val _selectedProfilePictureUri = mutableStateOf<Uri?>(null)
    val selectedProfilePictureUri: State<Uri?> = _selectedProfilePictureUri

    var isUpdating by mutableStateOf(false)

    fun onEvent(event: EditProfileEvent) {
        viewModel.onEvent(event)
    }

    fun updateSelectedImageUri(imageUri: Uri?) {
        _selectedProfilePictureUri.value = imageUri
    }

    fun uploadUserPictureAndUpdateProfile(
        selectedProfilePictureUri: Uri?, userId: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            isUpdating = true
            if (selectedProfilePictureUri != null) {
                val downloadUrl = firebaseStorageRepository.uploadUserAvatarToFirebaseStorage(
                    profilePictureUri = selectedProfilePictureUri, userId = userId
                )
                onEvent(EditProfileEvent.EditProfile(profilePictureUri = downloadUrl.toString()))
                _selectedProfilePictureUri.value = null
            } else {
                onEvent(EditProfileEvent.EditProfile())
            }
            isUpdating = false
        }
    }
}