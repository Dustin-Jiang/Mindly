package top.tsukino.llmdemo.feature.collect

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.tsukino.llmdemo.feature.common.MainController
import top.tsukino.llmdemo.feature.common.component.Scrim

@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun BoxScope.CreateRecordButton(
    mainController: MainController,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isStarted = remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val onPermissionGranted = {
        scope.launch {
            delay(300)
            if (!isPressed) return@launch
            isStarted.value = true
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            Log.d("CreateRecordButton", "Start recording")
            onStart()
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("CreateRecordButton", "RECORD_AUDIO permission granted!")
            onPermissionGranted()
        } else {
            Log.d("CreateRecordButton", "RECORD_AUDIO permission denied.")
            scope.launch {
                mainController.snackbarHostState.showSnackbar(
                    message = "请授予录音权限以使用此功能",
                    withDismissAction = true
                )
            }
        }
    }

    Scrim(
        onDismissRequest = {},
        visible = isStarted.value,
    )
    Box(
        modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)
    ) {
        ExtendedFloatingActionButton(
            expanded = true,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            onClick = {},
            text = {
                Text(
                    text = "收集思绪"
                )
            },
            icon = {
                Icon(
                    Icons.Filled.Mic,
                    "收集思绪"
                )
            },
            interactionSource = interactionSource
        )
    }

    val hasPermission = {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            if (!hasPermission()) {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                onPermissionGranted()
            }
        }
        if (!isPressed && isStarted.value) {
            isStarted.value = false
            Log.d("CreateRecordButton", "Stop recording")
            onStop()
        }
    }
}