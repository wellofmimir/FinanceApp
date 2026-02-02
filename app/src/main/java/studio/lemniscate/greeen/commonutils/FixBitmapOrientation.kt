package studio.lemniscate.greeen.commonutils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface

fun Bitmap.fixOrientation(path: String): Bitmap {

    val exif = ExifInterface(path)

    val rotation = when (exif.getAttributeInt (
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    if (rotation == 0f)
        return this

    val matrix = Matrix().apply {
        postRotate(rotation)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}