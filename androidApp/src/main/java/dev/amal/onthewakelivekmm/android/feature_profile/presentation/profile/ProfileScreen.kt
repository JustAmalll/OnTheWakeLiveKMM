package dev.amal.onthewakelivekmm.android.feature_profile.presentation.profile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.amal.onthewakelivekmm.android.R
import dev.amal.onthewakelivekmm.android.core.presentation.components.FormattedDateOfBirth
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardImageView
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardLoadingView
import dev.amal.onthewakelivekmm.android.navigation.Screen
import dev.amal.onthewakelivekmm.feature_profile.presentation.profile.ProfileEvent

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: AndroidProfileViewModel = hiltViewModel(),
    imageLoader: ImageLoader
) {
    val state by viewModel.state.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val haptic = LocalHapticFeedback.current

    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = surfaceColor, darkIcons = !darkTheme
        )
    }

    LaunchedEffect(key1 = state.error) {
        state.error?.let { error ->
            snackBarHostState.showSnackbar(message = error)
            viewModel.onEvent(ProfileEvent.OnProfileErrorSeen)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(ProfileEvent.GetProfile)
    }

    AnimatedContent(targetState = state.isLoading) { isLoading ->
        if (isLoading) StandardLoadingView()
        else state.profile?.let { profile ->
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(id = R.string.profile),
                                fontSize = 32.sp
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = surfaceColor,
                            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(Screen.QueueScreen.route) { inclusive = true }
                                    viewModel.onEvent(ProfileEvent.Logout)
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_logout),
                                    contentDescription = stringResource(
                                        id = R.string.arrow_back_icon
                                    )
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = surfaceColor),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 20.dp, bottom = 40.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                IconButton(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(end = 4.dp, top = 2.dp),
                                    onClick = {
                                        haptic.performHapticFeedback(
                                            HapticFeedbackType.TextHandleMove
                                        )
//                                navController.navigate(Screen.EditProfileScreen.route)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = stringResource(id = R.string.edit_icon)
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StandardImageView(
                                        imageLoader = imageLoader,
                                        model = profile.profilePictureUri,
                                        onUserAvatarClicked = { pictureUrl ->
//                                    if (pictureUrl.isNotEmpty()) navController.navigate(
//                                        Screen.FullSizeAvatarScreen.passPictureUrl(pictureUrl)
//                                    )
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = profile.firstName,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(1.dp))
                                        Text(
                                            text = profile.lastName,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Trick List",
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            IconButton(onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                        navController.navigate(Screen.AddTricksScreen.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = stringResource(id = R.string.right_arrow)
                                )
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 18.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = stringResource(id = R.string.instagram),
                                    fontSize = 22.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = profile.instagram.ifEmpty {
                                    stringResource(id = R.string.not_specified)
                                })
                            }
                            if (profile.instagram.isNotEmpty()) {
                                IconButton(onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
//                            context.openInstagramProfile(profile.instagram)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = stringResource(id = R.string.right_arrow)
                                    )
                                }
                            }
                        }
                        Divider(modifier = Modifier.padding(vertical = 18.dp))
                        Column {
                            Text(
                                text = stringResource(id = R.string.telegram),
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = profile.telegram.ifEmpty {
                                stringResource(id = R.string.not_specified)
                            })
                        }
                        Divider(modifier = Modifier.padding(vertical = 18.dp))
                        Column {
                            Text(
                                text = stringResource(id = R.string.phone_number),
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = profile.phoneNumber)
                        }
                        Divider(modifier = Modifier.padding(vertical = 18.dp))
                        FormattedDateOfBirth(profile.dateOfBirth)
                    }
                }
            }
        }
    }
}