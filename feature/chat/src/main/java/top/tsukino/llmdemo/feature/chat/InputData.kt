package top.tsukino.llmdemo.feature.chat

import android.net.Uri

data class InputData(
    val text: String,
    val images: ImageData,
) {
    companion object {
        fun empty(): InputData {
            return InputData(
                text = "",
                images = ImageData.empty()
            )
        }
    }
}

data class ImageData(
    val hasImage: Boolean,
    val images: List<Uri>,
) {
    companion object {
        fun empty(): ImageData {
            return ImageData(
                hasImage = false,
                images = emptyList<Uri>(),
            )
        }
    }
}
