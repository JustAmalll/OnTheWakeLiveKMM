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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.components.EmptyContent
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.components.QueueItem
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.components.TabLayout
import dev.amal.onthewakelivekmm.feature_queue.presentation.QueueSocketEvent
import dev.amal.onthewakelivekmm.feature_queue.presentation.QueueState

@ExperimentalPagerApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun QueueScreen(
    state: QueueState,
    imageLoader: ImageLoader,
    onEvent: (QueueSocketEvent) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val pagerState = rememberPagerState(pageCount = 2, initialPage = 1)
    val snackBarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)

    LaunchedEffect(key1 = state.error) {
        state.error?.let { error ->
            snackBarHostState.showSnackbar(message = error)
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
                    onEvent(
                        QueueSocketEvent.AddToQueue(
                            isLeftQueue = pagerState.currentPage == 0,
                            firstName = "First name",
                            timestamp = System.currentTimeMillis()
                        )
                    )
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabLayout(pagerState = pagerState)

            AnimatedContent(targetState = state.isQueueLoading) { isLoading ->
                if (isLoading) {
//                    Column {
//                        repeat(5) { AnimatedShimmer() }
//                    }
                } else HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> QueueLeftContent(
                            state = state,
                            imageLoader = imageLoader,
                            onDetailsClicked = { queueItemId ->

                            },
                            onUserAvatarClicked = { pictureUrl ->

                            }
                        )
                        1 -> QueueRightContent(
                            state = state,
                            imageLoader = imageLoader,
                            onDetailsClicked = { queueItemId ->

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
                        onDetailsClicked = onDetailsClicked,
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
                        onDetailsClicked = onDetailsClicked,
                        onUserAvatarClicked = onUserAvatarClicked
                    )
                }
            }
        }
    }
}