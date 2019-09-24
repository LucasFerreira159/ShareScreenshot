package com.app4funbr.screenshot.extensions

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Activity.requestPermissions(): Boolean {
    var permissions = false
    Dexter.withActivity(this)
        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                permissions = true
            }

            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                permissions = false
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest,
                token: PermissionToken
            ) {
                token.continuePermissionRequest()
            }
        }).check()
    return permissions
}

fun Activity.saveImageExternal(image: Bitmap): Uri? {
    var uri: Uri? = null
    try {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png")
        val stream = FileOutputStream(file)
        image.compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.close()
        uri = FileProvider.getUriForFile(this, this.applicationContext.packageName + ".provider", file)
    } catch (e: IOException) {
        Log.d("INFO", "IOException while trying to write file for sharing: " + e.message)
    }
    return uri
}

fun Activity.shareImageURI(uri: Uri){
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/jpeg"
    }

    startActivity(Intent.createChooser(shareIntent, "Enviar para"))
}