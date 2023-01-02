package dev.amal.onthewakelivekmm.android.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_login.AndroidLoginViewModel
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_login.LoginScreen
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.AndroidQueueViewModel
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.QueueScreen

@ExperimentalPagerApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    imageLoader: ImageLoader
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(route = Screen.QueueScreen.route) {
            val queueViewModel = hiltViewModel<AndroidQueueViewModel>()
            val state by queueViewModel.state.collectAsState()
            QueueScreen(state = state, imageLoader = imageLoader) { event ->
                queueViewModel.onEvent(event)
            }
        }
        composable(route = Screen.LoginScreen.route) {
            val loginViewModel = hiltViewModel<AndroidLoginViewModel>()
            val state by loginViewModel.state.collectAsState()
            LoginScreen(state = state) { event ->
                loginViewModel.onEvent(event)
            }
        }
    }
}