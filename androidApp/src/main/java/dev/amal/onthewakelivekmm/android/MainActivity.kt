package dev.amal.onthewakelivekmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import dev.amal.onthewakelivekmm.android.core.presentation.ui.theme.OnTheWakeLiveTheme
import dev.amal.onthewakelivekmm.android.navigation.SetupNavGraph
import javax.inject.Inject

@ExperimentalPagerApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnTheWakeLiveTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    imageLoader = imageLoader
                )
            }
        }
    }
}