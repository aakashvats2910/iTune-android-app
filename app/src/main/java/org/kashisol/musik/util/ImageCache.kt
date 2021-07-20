package org.kashisol.musik.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class ImageCache {

    companion object {

        // save an image in the dir.
        fun saveCacheImage(bitmap: Bitmap, name: String, context: Context) {
            // replace all the literals which are not allowed in the files name
            var cleanName = name.replace("/".toRegex(), "_")
            cleanName = cleanName.replace(":".toRegex(), "_")
            // create the directory to save images if not present
            val cacheDir = File(context.filesDir, "/cache_img")
            if (!cacheDir.exists())
                cacheDir.mkdir()
            // create the image file if not exists
            val file = File(context.filesDir, "/cache_img/$cleanName")
            try {
                if (!file.exists()) {
                    file.createNewFile()
                }
            } catch (e: Exception) {
                println("()()()() SAVING IMG EXCEPTION : " + e.toString())
            }
            // after creating the file open the file and pass the image in it.
            try {
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
                }
            } catch (e: Exception) {
                println("()()()() CACHE_SAVING_IMAGE : $e")
            }
        }

        // retrieve an image from the dir (if exists?)
        fun getCacheImage(name: String, context: Context): Bitmap? {
            // replace all the literals which are not allowed in the files name
            var cleanName = name.replace("/".toRegex(), "_")
            cleanName = cleanName.replace(":".toRegex(), "_")
            // check if the folder exists
            var cacheDir = File(context.filesDir, "/cache_img")
            if (!cacheDir.exists())
                return null
            // if the folder exists check if the image exists or not.
            var file = File(context.filesDir, "/cache_img/$cleanName")
            if (file.exists()) {
                // if the file is found return the bitmap
                return BitmapFactory.decodeFile(file.absolutePath)
            }
            // return null if file not found
            return null
        }

        // For testing purposes.
        fun createTestFile(context: Context) {
            val cacheDir = File(context.filesDir, "/cache_img")
            if (!cacheDir.exists())
                cacheDir.mkdir()
            val file = File(context.filesDir, "/cache_img/https___is5-ssl.mzstatic.com_image_thumb_Video22_v4_0b_3a_59_0b3a598c-f7b8-eb9a-d0d0-d1a28a5ec76e_source_100x100bb.jpg")
            file.createNewFile()
        }

    }

}