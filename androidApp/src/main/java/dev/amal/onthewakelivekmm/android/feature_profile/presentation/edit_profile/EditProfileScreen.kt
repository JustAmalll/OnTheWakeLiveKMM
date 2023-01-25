package dev.amal.onthewakelivekmm.android.feature_profile.presentation.edit_profile

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.amal.onthewakelivekmm.android.R
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardLoadingView
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardTextField
import dev.amal.onthewakelivekmm.android.feature_profile.presentation.edit_profile.components.UserAvatarItem
import dev.amal.onthewakelivekmm.feature_profile.presentation.edit_profile.EditProfileEvent.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun EditProfileScreen(
    viewModel: AndroidEditProfileViewModel = hiltViewModel(),
    imageLoader: ImageLoader,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    val profilePictureUri = viewModel.selectedProfilePictureUri.value

    val snackBarHostState = remember { SnackbarHostState() }

    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
    val bgColor = MaterialTheme.colorScheme.background
    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = surfaceColor, darkIcons = !darkTheme
        )
        systemUiController.setNavigationBarColor(
            color = bgColor, darkIcons = !darkTheme
        )
    }

    LaunchedEffect(key1 = state.resultMessage) {
        state.resultMessage?.let { resultMessage ->
            snackBarHostState.showSnackbar(message = resultMessage)
            viewModel.onEvent(OnResultSeen)
        }
    }

    AnimatedContent(targetState = viewModel.isUpdating || state.isLoading) { isLoading ->
        if (isLoading) StandardLoadingView()
        else Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = R.string.edit_profile)) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = surfaceColor,
                        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.arrow_back_icon)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(bgColor)
                    .padding(horizontal = 24.dp)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = 24.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserAvatarItem(
                    imageLoader = imageLoader,
                    profilePictureUri = profilePictureUri ?: state.profilePictureUri.toUri(),
                    onAvatarCropped = { uri ->
                        viewModel.updateSelectedImageUri(uri)
                    }
                )
                Spacer(modifier = Modifier.height(30.dp))
                StandardTextField(
                    value = state.firstName,
                    onValueChange = {
                        viewModel.onEvent(EditProfileFirstNameChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    label = stringResource(id = R.string.first_name),
                    errorText = state.firsNameError
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.lastName,
                    onValueChange = {
                        viewModel.onEvent(EditProfileLastNameChanged(it))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    label = stringResource(id = R.string.last_name),
                    errorText = state.lastNameError
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.telegram,
                    onValueChange = {
                        viewModel.onEvent(EditProfileTelegramChanged(it))
                    },
                    label = stringResource(id = R.string.telegram)
                )
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextField(
                    value = state.instagram,
                    onValueChange = {
                        viewModel.onEvent(EditProfileInstagramChanged(it))
                    },
                    label = stringResource(id = R.string.instagram)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.dateOfBirth,
                    onValueChange = {
                        if (it.length <= 8) {
                            viewModel.onEvent(EditProfileDateOfBirthChanged(it))
                        }
                    },
                    label = {
                        Text(text = stringResource(id = R.string.date_of_birth_placeholder))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                state.dateOfBirthError?.let { dateOfBirthError ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = dateOfBirthError,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp,
                        textAlign = TextAlign.End
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.uploadUserPictureAndUpdateProfile(
                            selectedProfilePictureUri = profilePictureUri,
                            userId = state.userId
                        )
                        focusManager.clearFocus()
                    }
                ) {
                    Text(text = stringResource(id = R.string.edit))
                }
            }
        }
    }
}