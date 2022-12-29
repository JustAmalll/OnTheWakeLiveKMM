package dev.amal.onthewakelivekmm.android.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.AndroidQueueViewModel
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.QueueScreen

@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    viewModel: AndroidQueueViewModel,
    navController: NavHostController,
    imageLoader: ImageLoader
) {
    NavHost(
        navController = navController,
        startDestination = Screen.QueueScreen.route
    ) {
        composable(route = Screen.QueueScreen.route) {
            val state by viewModel.state.collectAsState()
            QueueScreen(state = state, imageLoader = imageLoader)
        }
    }
}