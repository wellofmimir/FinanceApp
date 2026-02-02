package studio.lemniscate.greeen.commonutils

import java.io.File
import android.content.Context

class FileProvider (
    private val context: Context
) {
    fun getPNG(name: String = ""): File {
        return File(context.filesDir, name.ifEmpty { "image_${System.currentTimeMillis()}.png" })
    }

    fun getWallpaper(name: String = ""): File {
        val wallpapersDirectory = File(context.filesDir, "wallpapers")

        if (!wallpapersDirectory.exists())
            wallpapersDirectory.mkdirs()

        return File(context.filesDir, name.ifEmpty { "image_${System.currentTimeMillis()}.png" })
    }
}