package dev.amal.onthewakelivekmm.android.navigation

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
    object QueueScreen : Screen("queue_screen")
}