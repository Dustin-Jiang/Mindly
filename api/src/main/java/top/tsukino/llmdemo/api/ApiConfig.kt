package top.tsukino.llmdemo.api

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.logging.LogLevel
import com.aallam.openai.api.logging.Logger
import com.aallam.openai.client.LoggingConfig
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.aallam.openai.client.ProxyConfig
import com.aallam.openai.client.RetryStrategy
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import kotlin.time.Duration.Companion.seconds

public class ApiConfig(
    public val token: String,
    public val host: String,
) {
    internal fun toOpenAIConfig(): OpenAIConfig {
        var url = host
        if (!host.endsWith("/")) {
            url += "/"
        }
        return OpenAIConfig(
            token = token,
            host = OpenAIHost(url),
        )
    }
}