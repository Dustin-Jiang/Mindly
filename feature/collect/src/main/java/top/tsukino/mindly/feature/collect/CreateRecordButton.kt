package top.tsukino.mindly.feature.collect

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import top.tsukino.mindly.feature.common.MainController
import top.tsukino.mindly.feature.common.component.Scrim

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
    val isLocked = remember { mutableStateOf(false) }
    val isStarted = remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val duration = remember { mutableLongStateOf(0L) }

    val onPermissionGranted = {
        scope.launch {
            duration.value = 0L
            delay(300)
            if (!isPressed) return@launch

            isStarted.value = true
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            Log.d("CreateRecordButton", "Start recording")
            onStart()
            // Lock after a delay for long recordings
            delay(10000)
            if (!isPressed) return@launch

            Log.d("CreateRecordButton", "Locking recording")
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            isLocked.value = true
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

    fun stop() {
        isStarted.value = false
        Log.d("CreateRecordButton", "Stop recording")
        onStop()
    }

    Scrim(
        onDismissRequest = {},
        visible = isStarted.value,
    )
    Box(
        modifier = Modifier.
            fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = isStarted.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 48.dp).align(Alignment.TopCenter)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.large,
                        shadowElevation = 8.dp,
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            AnimatedVisibility(isLocked.value) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    contentDescription = "录音已锁定",
                                )
                            }
                            Icon(
                                imageVector = Icons.Filled.RadioButtonChecked,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                contentDescription = "录音中",
                            )
                            Text(
                                text = duration.longValue.toDurationString(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)
        ) {
            val containerColor = when {
                isLocked.value -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.primary
            }
            val contentColor = when {
                isLocked.value -> MaterialTheme.colorScheme.onSecondary
                else -> MaterialTheme.colorScheme.onPrimary
            }
            val shape = when {
                isLocked.value -> MaterialTheme.shapes.extraLarge
                else -> FloatingActionButtonDefaults.extendedFabShape
            }
            val text = when {
                isLocked.value -> "结束"
                else -> "收集思绪"
            }
            val icon = when {
                isLocked.value -> Icons.Filled.StopCircle
                else -> Icons.Filled.Mic
            }
            Column {
                ExtendedFloatingActionButton(
                    expanded = true,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    onClick = {},
                    shape = shape,
                    text = {
                        Text(
                            text = text
                        )
                    },
                    icon = {
                        Icon(
                            icon,
                            text
                        )
                    },
                    interactionSource = interactionSource
                )
            }
        }
    }

    val hasPermission = {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            if (isStarted.value && isLocked.value) {
                isLocked.value = false
                stop()
                return@LaunchedEffect
            }
            if (!hasPermission()) {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                onPermissionGranted()
            }
        }
        if (!isPressed && isStarted.value && !isLocked.value) {
            stop()
        }
    }

    LaunchedEffect(isStarted.value) {
        while (isStarted.value) {
            delay(1000L)
            duration.longValue += 1L
        }
    }
}

internal fun Long.toDurationString(): String {
    val seconds = this
    val minutes = seconds / 60
    val hours = minutes / 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
    } else {
        String.format("%02d:%02d", minutes, seconds % 60)
    }
}