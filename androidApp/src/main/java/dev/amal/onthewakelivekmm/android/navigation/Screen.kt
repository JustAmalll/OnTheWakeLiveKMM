package dev.amal.onthewakelivekmm.android.navigation

import dev.amal.onthewakelivekmm.android.core.utils.Constants
import dev.amal.onthewakelivekmm.android.core.utils.Constants.REGISTER_DATA_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object OtpScreen : Screen("otp_screen/{$REGISTER_DATA_ARGUMENT_KEY}") {
        fun passRegisterData(registerData: String): String = "otp_screen/$registerData"
    }
    object QueueScreen : Screen("queue_screen")
    object QueueDetailsScreen : Screen(
        "queue_details_screen/{${Constants.DETAILS_ARGUMENT_KEY}}"
    ) {
        fun passItemId(itemId: String): String = "queue_details_screen/$itemId"
    }
    object ProfileScreen : Screen("profile_screen")
    object EditProfileScreen : Screen("edit_profile_screen")
}