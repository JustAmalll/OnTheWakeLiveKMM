package dev.amal.onthewakelivekmm.android.core.presentation.components

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.navigation.NavHostController
import dev.amal.onthewakelivekmm.android.core.domain.modules.BottomNavItem
import dev.amal.onthewakelivekmm.android.navigation.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun StandardScaffold(
    navController: NavHostController,
    showBottomBar: Boolean = true,
    content: @Composable () -> Unit
) {

    val haptic = LocalHapticFeedback.current

    val bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem(
            route = Screen.QueueScreen.route,
            icon = Icons.Default.Home,
            contentDescription = "Queue"
        ),
        BottomNavItem(
            route = Screen.ProfileScreen.route,
            icon = Icons.Default.Person,
            contentDescription = "Profile"
        )
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) NavigationBar {
                bottomNavItems.forEachIndexed { _, item ->
                    NavigationBarItem(
                        icon = {
                            item.icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = item.contentDescription
                                )
                            }
                        },
                        label = { item.contentDescription?.let { Text(text = it) } },
                        selected = navController.currentDestination?.route?.startsWith(
                            item.route
                        ) == true,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (navController.currentDestination?.route != item.route) {
                                navController.navigate(item.route) { launchSingleTop = true }
                            }
                        }
                    )
                }
            }
        }
    ) {
        content()
    }
}