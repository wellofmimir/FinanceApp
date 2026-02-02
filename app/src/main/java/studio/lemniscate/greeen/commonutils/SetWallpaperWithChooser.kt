package studio.lemniscate.greeen.commonutils

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import android.content.ClipData

import java.io.File
import java.io.FileOutputStream
import android.graphics.Bitmap

fun setWallpaperWithChooser (
    context: Context,
    bitmap: Bitmap
) {
    val file = File(context.cacheDir, "wallpaper_temp.png")

    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val intent = Intent(WallpaperManager.ACTION_CROP_AND_SET_WALLPAPER).apply {
        setDataAndType(uri, "image/*")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        clipData = ClipData.newUri(context.contentResolver, "wallpaper", uri)
    }

    context.startActivity(Intent.createChooser(intent, "Set Wallpaper"))
}
