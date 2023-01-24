package dev.amal.onthewakelivekmm.android.feature_splash.presentation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.amal.onthewakelivekmm.android.R
import dev.amal.onthewakelivekmm.android.navigation.Screen
import dev.amal.onthewakelivekmm.feature_auth.domain.models.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@ExperimentalAnimationApi
@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: AndroidSplashViewModel = hiltViewModel()
) {
    var isServerUnavailable by remember { mutableStateOf(false) }

    val scale = remember { Animatable(0f) }
    val overshootInterpolator = remember { OvershootInterpolator(2f) }

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.background

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = backgroundColor, darkIcons = !darkTheme
        )
    }

    LaunchedEffect(key1 = true) {
        withContext(Dispatchers.Main) {
            scale.animateTo(
                targetValue = 0.5f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = { overshootInterpolator.getInterpolation(it) }
                )
            )
        }
    }
    LaunchedEffect(key1 = true) {
        delay(1000)
        viewModel.state.collect { authResult ->
            when (authResult) {
                AuthResult.Authorized -> navController.navigate(Screen.QueueScreen.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true }
                }
                AuthResult.Unauthorized -> navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(Screen.SplashScreen.route) { inclusive = true }
                }
                else -> isServerUnavailable = true
            }
        }
    }

    AnimatedContent(targetState = isServerUnavailable) { isError ->
        if (isError) ErrorContent()
        else Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier
                    .size(600.dp)
                    .scale(scale.value),
                painter = painterResource(
                    id = if (darkTheme) R.drawable.logo_white else R.drawable.logo_black
                ),
                contentDescription = stringResource(id = R.string.logo)
            )
        }
    }
}

@Composable
fun ErrorContent() {
    val compositionResult: LottieCompositionResult =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.server_error))

    val progress by animateLottieCompositionAsState(
        composition = compositionResult.value,
        iterations = LottieConstants.IterateForever,
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LottieAnimation(
            composition = compositionResult.value,
            progress = { progress },
            modifier = Modifier.size(240.dp)
        )
        Text(
            text = "Server Unavailable",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = "The server is temporarily busy, try again later!",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}