package dev.amal.onthewakelivekmm.android.feature_queue.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyContent(modifier: Modifier = Modifier) {

//    val compositionResult: LottieCompositionResult =
//        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.cat))
//
//    val progress by animateLottieCompositionAsState(
//        composition = compositionResult.value,
//        isPlaying = true,
//        iterations = LottieConstants.IterateForever,
//        speed = 1.0f
//    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
//        LottieAnimation(
//            composition = compositionResult.value,
//            progress = { progress },
//            modifier = Modifier.size(240.dp)
//        )
        Text(
            text = "There is no one in the queue",
            fontSize = 17.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}