package com.example.financeapp.commonutils

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.net.Uri
import androidx.annotation.RequiresPermission
import androidx.compose.ui.platform.LocalContext

@RequiresPermission(Manifest.permission.SET_WALLPAPER)
fun setWallpaper (
    context: Context,
    bitmapUri: Uri
) {
    val intent = WallpaperManager.getInstance(context)
        .getCropAndSetWallpaperIntent(bitmapUri)

    context.startActivity(intent)
}