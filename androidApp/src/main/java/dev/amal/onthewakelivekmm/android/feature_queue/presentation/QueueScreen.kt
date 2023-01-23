package dev.amal.onthewakelivekmm.android.feature_queue.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.amal.onthewakelivekmm.android.core.presentation.components.AnimatedShimmer
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.components.*
import dev.amal.onthewakelivekmm.core.util.Constants.ADMIN_IDS
import dev.amal.onthewakelivekmm.feature_queue.presentation.queue.QueueEvent
import dev.amal.onthewakelivekmm.feature_queue.presentation.queue.QueueEvent.AddToQueue
import dev.amal.onthewakelivekmm.feature_queue.presentation.queue.QueueState

@ExperimentalPagerApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun QueueScreen(
    viewModel: AndroidQueueViewModel = hiltViewModel(),
    imageLoader: ImageLoader
) {
    val state by viewModel.state.collectAsState()

    var showAdminDialog by remember { mutableStateOf(false) }

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var queueItemIdToDelete by remember { mutableStateOf("") }

    val haptic = LocalHapticFeedback.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val pagerState = rememberPagerState(pageCount = 2, initialPage = 1)
    val snackBarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)

    LaunchedEffect(key1 = state.error) {
        state.error?.let { error ->
            snackBarHostState.showSnackbar(message = error)
            viewModel.onEvent(QueueEvent.OnQueueErrorSeen)
        }
    }

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = surfaceColor, darkIcons = !darkTheme
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Queue",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        floatingActionButton = {
            if (!state.isQueueLoading) FloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    if (state.userId in ADMIN_IDS) showAdminDialog = true
                    else viewModel.onEvent(AddToQueue(isLeftQueue = pagerState.currentPage == 0))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Icon",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    viewModel.onEvent(QueueEvent.InitSession)
                }
                else if (event == Lifecycle.Event.ON_PAUSE) {
                    viewModel.onEvent(QueueEvent.CloseSession)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        if (showConfirmationDialog) ConfirmationDialog(
            showDialog = { showConfirmationDialog = it },
            isUserAdmin = state.userId in ADMIN_IDS,
            onLeaveQueue = {
                viewModel.onEvent(
                    QueueEvent.DeleteQueueItem(
                        queueItemId = queueItemIdToDelete
                    )
                )
            }
        )

        if (showAdminDialog) AdminDialog(
            showDialog = { showAdminDialog = it },
            queue = state.queue,
            onAddClicked = { isLeftQueue, firstName ->
                viewModel.onEvent(
                    event = AddToQueue(
                        isLeftQueue = isLeftQueue,
                        firstName = firstName
                    )
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabLayout(pagerState = pagerState)

            AnimatedContent(targetState = state.isQueueLoading) { isLoading ->
                if (isLoading) AnimatedShimmer()
                else HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> QueueLeftContent(
                            state = state,
                            imageLoader = imageLoader,
                            onDetailsClicked = { queueItemId ->

                            },
                            onSwipeToDelete = { queueItemId ->
                                showConfirmationDialog = true
                                queueItemIdToDelete = queueItemId
                            },
                            onUserAvatarClicked = { pictureUrl ->

                            }
                        )
                        1 -> QueueRightContent(
                            state = state,
                            imageLoader = imageLoader,
                            onDetailsClicked = { queueItemId ->

                            },
                            onSwipeToDelete = { queueItemId ->
                                showConfirmationDialog = true
                                queueItemIdToDelete = queueItemId
                            },
                            onUserAvatarClicked = { pictureUrl ->

                            }
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun QueueLeftContent(
    state: QueueState,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit,
    onUserAvatarClicked: (String) -> Unit
) {
    val leftQueue = remember(state.queue) {
        state.queue.filter { it.isLeftQueue }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(targetState = leftQueue.isEmpty()) { isLeftQueueEmpty ->
            if (isLeftQueueEmpty) EmptyContent(modifier = Modifier.align(Alignment.Center))
            else LazyColumn(
                contentPadding = PaddingValues(10.dp),
                reverseLayout = true
            ) {
                items(leftQueue) { item ->
                    QueueItem(
                        queueItem = item,
                        imageLoader = imageLoader,
                        userId = state.userId,
                        onDetailsClicked = onDetailsClicked,
                        onSwipeToDelete = onSwipeToDelete,
                        onUserAvatarClicked = onUserAvatarClicked
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun QueueRightContent(
    state: QueueState,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit,
    onUserAvatarClicked: (String) -> Unit
) {
    val rightQueue = remember(state.queue) {
        state.queue.filter { !it.isLeftQueue }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(targetState = rightQueue.isEmpty()) { isRightQueueEmpty ->
            if (isRightQueueEmpty) EmptyContent(modifier = Modifier.align(Alignment.Center))
            else LazyColumn(
                contentPadding = PaddingValues(10.dp),
                reverseLayout = true
            ) {
                items(rightQueue) { item ->
                    QueueItem(
                        queueItem = item,
                        imageLoader = imageLoader,
                        userId = state.userId,
                        onDetailsClicked = onDetailsClicked,
                        onSwipeToDelete = onSwipeToDelete,
                        onUserAvatarClicked = onUserAvatarClicked
                    )
                }
            }
        }
    }
}