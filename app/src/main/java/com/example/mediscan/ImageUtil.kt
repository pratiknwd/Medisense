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
        
//        private fun showImagePickerDialog(context: Context) {
//            val options = arrayOf("Take Photo", "Choose from Gallery")
//            AlertDialog.Builder(context)
//                .setTitle("Select Image")
//                .setItems(options) { _, which ->
//                    when (which) {
//                        0 -> openCamera(context)
//                        1 -> openGallery()
//                    }
//                }
//                .show()
//        }
//
//
//        private fun openCamera(context: Context) {
//            imageUri = createImageUri(context)
//            takePictureLauncher.launch(imageUri)
//        }
//
//        private fun openGallery() {
//            pickImageLauncher.launch("image/*")
//        }
//
//        private fun createImageUri(context: Context): Uri {
//            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${System.currentTimeMillis()}.jpg")
//            return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
//        }
    }
}