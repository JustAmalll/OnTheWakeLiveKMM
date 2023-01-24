package dev.amal.onthewakelivekmm.android.feature_profile.presentation.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.amal.onthewakelivekmm.android.R
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardLoadingView
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardTextField
import dev.amal.onthewakelivekmm.android.core.utils.CropActivityResultContract
import dev.amal.onthewakelivekmm.feature_profile.presentation.edit_profile.EditProfileEvent

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun EditProfileScreen(
    viewModel: AndroidEditProfileViewModel = hiltViewModel(),
    imageLoader: ImageLoader,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    val isImageLoading = remember { mutableStateOf(false) }

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
            viewModel.onEvent(EditProfileEvent.OnResultSeen)
        }
    }

    val profilePictureUri = viewModel.selectedProfilePictureUri.value

    val cropActivityLauncher = rememberLauncherForActivityResult(
        contract = CropActivityResultContract(16f, 16f)
    ) { viewModel.updateSelectedImageUri(it) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { if (it != null) cropActivityLauncher.launch(it) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    AnimatedContent(targetState = viewModel.isUpdating || state.isLoading) { isLoading ->
        if (isLoading) StandardLoadingView()
        else Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(bgColor)
                            .padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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
                                            model = profilePictureUri ?: state.profilePictureUri,
                                            imageLoader = imageLoader,
                                            onLoading = { isImageLoading.value = true },
                                            onError = { isImageLoading.value = false },
                                            onSuccess = { isImageLoading.value = false }
                                        ),
                                        contentDescription = stringResource(id = R.string.user_picture)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            StandardTextField(
                                value = state.firstName,
                                onValueChange = {
                                    viewModel.onEvent(
                                        EditProfileEvent.EditProfileFirstNameChanged(it)
                                    )
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
                                    viewModel.onEvent(
                                        EditProfileEvent.EditProfileLastNameChanged(it)
                                    )
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
                                    viewModel.onEvent(
                                        EditProfileEvent.EditProfileTelegramChanged(it)
                                    )
                                },
                                label = stringResource(id = R.string.telegram)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            StandardTextField(
                                value = state.instagram,
                                onValueChange = {
                                    viewModel.onEvent(
                                        EditProfileEvent.EditProfileInstagramChanged(it)
                                    )
                                },
                                label = stringResource(id = R.string.instagram)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = state.dateOfBirth,
                                onValueChange = {
                                    if (it.length <= 8) {
                                        viewModel.onEvent(
                                            EditProfileEvent.EditProfileDateOfBirthChanged(it)
                                        )
                                    }
                                },
                                label = {
                                    Text(
                                        text = stringResource(
                                            id = R.string.date_of_birth_placeholder
                                        )
                                    )
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
        }
    }
}