package top.tsukino.mindly.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import top.tsukino.mindly.feature.theme.LLMDemoTheme

@AndroidEntryPoint
class MainActivity :
    ComponentActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )
        enableEdgeToEdge()
        setContent {
            LLMDemoTheme {
                MainApp()
            }
        }
    }
}
