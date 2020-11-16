package com.ahmedmolawale.androidnanohttpd

import android.content.Context
import android.os.StatFs
import android.util.Log
import androidx.core.content.ContextCompat
import fi.iki.elonen.NanoHTTPD
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class LocalWebserver(context: Context, port: Int) : NanoHTTPD(port) {

    var context: Context? = context
    val MIME_JAVASCRIPT = "text/javascript"
    val MIME_CSS = "text/css"
    val MIME_JPEG = "image/jpeg"
    val MIME_PNG = "image/png"
    val MIME_SVG = "image/svg+xml"
    val MIME_JSON = "application/json"
    val MIME_GIF = "image/gif"
    val MIME_BMP = "image/bmp"
    var mimeType = MIME_HTML
    val folderName = "portfolio" //name of the folder holding the asset of the page you wanna load

    override fun serve(session: IHTTPSession?): Response? {
        val uri = session?.uri
        try {
            when {
                uri!!.endsWith(".js") -> {
                    mimeType = MIME_JAVASCRIPT
                }
                uri.endsWith(".css") -> {
                    mimeType = MIME_CSS
                }
                uri.endsWith(".html") -> {
                    mimeType = MIME_HTML
                }
                uri.endsWith(".jpeg") -> {
                    mimeType = MIME_JPEG
                }
                uri.endsWith(".png") -> {
                    mimeType = MIME_PNG
                }
                uri.endsWith(".jpg") -> {
                    mimeType = MIME_JPEG
                }
                uri.endsWith(".svg") -> {
                    mimeType = MIME_SVG
                }
                uri.endsWith(".bmp") -> {
                    mimeType = MIME_BMP
                }
                uri.endsWith(".gif") -> {
                    mimeType = MIME_GIF
                }
                uri.endsWith(".json") -> {
                    mimeType = MIME_JSON
                }
            }
        } catch (e: Exception) {
        }
        val root = "${getStoragePath()}${File.separator}"
        var fis: FileInputStream? = null
        val file = File(
            root +
                    "${folderName}/${uri}"
        )
        try {
            if (file.exists()) {
                fis = FileInputStream(file);
            }
        } catch (ioe: IOException) {
            Log.e("Httpd %s", ioe.toString())
        }
        return newFixedLengthResponse(
            Response.Status.OK,
            mimeType,
            fis,
            file.length()
        )
    }

    private fun getStoragePath(): String? {
        var path: String? = null
        var space: Long = 0
        val files: Array<File> = ContextCompat.getExternalFilesDirs(context!!, null)
        // go through the options to choose one with more available storage capacity
        for (f in files) {
            val stat = StatFs(f.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong

            // check if storage capacity is more than the previous one
            if (totalBlocks * blockSize > space) {
                space = totalBlocks * blockSize
                path = f.path
            }
        }
        return path
    }
}