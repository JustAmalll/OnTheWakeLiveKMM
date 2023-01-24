package dev.amal.onthewakelivekmm.android.core.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import dev.amal.onthewakelivekmm.android.core.presentation.components.StandardScaffold
import dev.amal.onthewakelivekmm.android.core.presentation.ui.theme.OnTheWakeLiveTheme
import dev.amal.onthewakelivekmm.android.core.utils.isUserAdmin
import dev.amal.onthewakelivekmm.android.navigation.Screen
import dev.amal.onthewakelivekmm.android.navigation.SetupNavGraph
import dev.amal.onthewakelivekmm.core.data.cache.PreferenceManager
import dev.amal.onthewakelivekmm.core.util.Constants
import javax.inject.Inject

@ExperimentalPagerApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var preferencesManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnTheWakeLiveTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val isUserAdmin = preferencesManager.getString(Constants.PREFS_USER_ID).isUserAdmin()

                StandardScaffold(
                    navController = navController,
                    showBottomBar = navBackStackEntry?.destination?.route in listOf(
                        Screen.QueueScreen.route, Screen.ProfileScreen.route
                    ) && !isUserAdmin
                ) {
                    SetupNavGraph(navController = navController, imageLoader = imageLoader)
                }
            }
        }
    }
}