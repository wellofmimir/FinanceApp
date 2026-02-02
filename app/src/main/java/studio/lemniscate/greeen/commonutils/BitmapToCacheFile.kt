package studio.lemniscate.greeen.commonutils

import java.io.File
import android.content.Context
import android.graphics.Bitmap
import java.io.FileOutputStream

fun bitmapToCachePNGFile (
    context: Context,
    bitmap: Bitmap,
    name: String
): File {

    val file = File(context.cacheDir, "$name.png")

    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    return file
}