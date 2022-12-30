package dev.amal.onthewakelivekmm.android.feature_queue.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun TabLayout(pagerState: PagerState) {

    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val list = listOf(
        "Left Line" to Icons.Default.ArrowBack,
        "Right Line" to Icons.Default.ArrowForward
    )

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                icon = {
                    Icon(
                        imageVector = list[index].second,
                        contentDescription = null,
                        tint = if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                text = {
                    Text(
                        list[index].first,
                        color = if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    scope.launch { pagerState.animateScrollToPage(index) }
                }
            )
        }
    }
}