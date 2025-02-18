package com.example.mediscan

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri

class ImageUtil {
    companion object {
        fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            return ImageDecoder.decodeBitmap(source)
        }
        
    }
}