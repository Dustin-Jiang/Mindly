package top.tsukino.llmdemo.feature

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import top.tsukino.llmdemo.feature.sharereceiver.ShareReceiverScreen

@Composable
fun ShareReceiverApp(
    isShare: Boolean,
    shareText: String? = null,
    mime: String? = null,
) {
    val systemUiController = rememberSystemUiController()

    val backgroundColor = MaterialTheme.colorScheme.surface
    val bottomNavBarColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val darkTheme = isSystemInDarkTheme()

    LaunchedEffect(bottomNavBarColor, darkTheme) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !darkTheme
        )

        systemUiController.setNavigationBarColor(
            color = bottomNavBarColor,
        )
    }
    Log.d("ShareReceiverApp", "isShare: $isShare, mime: $mime")

    if (!isShare) return

    ShareReceiverScreen(
        shareText = shareText,
    )
}