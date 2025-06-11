package top.tsukino.mindly.api

import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost

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