package dev.amal.onthewakelivekmm.android.navigation

sealed class Screen(val route: String) {
    object QueueScreen : Screen("queue_screen")
}