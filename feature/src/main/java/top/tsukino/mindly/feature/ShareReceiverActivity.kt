package top.tsukino.mindly.feature

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.AndroidEntryPoint
import top.tsukino.mindly.feature.theme.LLMDemoTheme

@AndroidEntryPoint
class ShareReceiverActivity: ComponentActivity() {
    private val isShare = mutableStateOf<Boolean>(false)
    private val mime = mutableStateOf<String?>(null)
    private val shareText = mutableStateOf<String?>(null)

    override fun onCreate(
        savedInstanceState: android.os.Bundle?
    ) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        isShare.value = false
        mime.value = null

        when {
            intent?.action == Intent.ACTION_SEND -> {
                mime.value = intent.type
                if (intent.type?.startsWith("text/") == true) {
                    handleSendText(intent)
                }
//                else if (intent.type?.startsWith("image/") == true) {
//                    handleSendImage(intent)
//                }
            }
        }

        if (isShare.value) {
            setContent {
                LLMDemoTheme {
                    ShareReceiverApp(
                        isShare = isShare.value,
                        mime = mime.value,
                        shareText = shareText.value,
                    )
                }
            }
        }
    }

    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            isShare.value = true
            shareText.value = it
        }
    }

//    private fun handleSendImage(intent: Intent) {
//        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
//            // Update UI to reflect image being shared
//        }
//    }
}