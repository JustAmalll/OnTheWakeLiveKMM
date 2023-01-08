package dev.amal.onthewakelivekmm.android.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import dev.amal.onthewakelivekmm.android.core.presentation.utils.Constants.REGISTER_DATA_ARGUMENT_KEY
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_login.LoginScreen
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.presentation.OtpScreen
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_register.presentation.RegisterScreen
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
            QueueScreen(imageLoader = imageLoader)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(
            route = Screen.OtpScreen.route,
            arguments = listOf(navArgument(REGISTER_DATA_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            OtpScreen(navController = navController)
        }
    }
}