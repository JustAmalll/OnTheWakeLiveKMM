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
import dev.amal.onthewakelivekmm.android.core.utils.Constants.DETAILS_ARGUMENT_KEY
import dev.amal.onthewakelivekmm.android.core.utils.Constants.PICTURE_URL_ARGUMENT_KEY
import dev.amal.onthewakelivekmm.android.core.utils.Constants.REGISTER_DATA_ARGUMENT_KEY
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_login.LoginScreen
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_otp.presentation.OtpScreen
import dev.amal.onthewakelivekmm.android.feature_auth.presentation.auth_register.presentation.RegisterScreen
import dev.amal.onthewakelivekmm.android.feature_full_size_avatar.presentation.FullSizeAvatarScreen
import dev.amal.onthewakelivekmm.android.feature_profile.presentation.edit_profile.EditProfileScreen
import dev.amal.onthewakelivekmm.android.feature_profile.presentation.profile.ProfileScreen
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.queue.QueueScreen
import dev.amal.onthewakelivekmm.android.feature_queue.presentation.queue_item_details.QueueItemDetailsScreen
import dev.amal.onthewakelivekmm.android.feature_splash.presentation.SplashScreen

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
        startDestination = Screen.SplashScreen.route
    ) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
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
        composable(route = Screen.QueueScreen.route) {
            QueueScreen(navController = navController, imageLoader = imageLoader)
        }
        composable(
            route = Screen.QueueDetailsScreen.route,
            arguments = listOf(navArgument(DETAILS_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            QueueItemDetailsScreen(imageLoader = imageLoader, navController = navController)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController, imageLoader = imageLoader)
        }
        composable(route = Screen.EditProfileScreen.route) {
            EditProfileScreen(imageLoader = imageLoader, navController = navController)
        }
        composable(
            route = Screen.FullSizeAvatarScreen.route,
            arguments = listOf(navArgument(PICTURE_URL_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        )  {
            FullSizeAvatarScreen(navController = navController, imageLoader = imageLoader)
        }
    }
}