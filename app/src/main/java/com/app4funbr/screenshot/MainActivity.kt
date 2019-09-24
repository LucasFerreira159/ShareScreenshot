package com.app4funbr.screenshot

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app4funbr.screenshot.extensions.requestPermissions
import com.app4funbr.screenshot.extensions.saveImageExternal
import com.app4funbr.screenshot.extensions.shareImageURI
import com.tarek360.instacapture.Instacapture
import com.tarek360.instacapture.listener.SimpleScreenCapturingListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var permissions = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissions = requestPermissions()

        button_screenshot.setOnClickListener {
            when (permissions) {
                true -> takeAndShareScreenShot()
                else -> showError()
            }
        }
    }

    fun takeAndShareScreenShot() {
        Instacapture.capture(this, object : SimpleScreenCapturingListener() {
            override fun onCaptureComplete(bitmap: Bitmap) {
                val uri = saveImageExternal(bitmap)
                uri?.let {
                    shareImageURI(uri)
                } ?: Toast.makeText(
                    applicationContext,
                    "Não foi possível compartilhar screenshot",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun showError() {
        Toast.makeText(
            this,
            "Necessário conceder as permissões",
            Toast.LENGTH_SHORT
        ).show()
        requestPermissions()
    }
}
