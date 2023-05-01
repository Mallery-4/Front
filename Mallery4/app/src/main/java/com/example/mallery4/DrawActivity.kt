package com.example.mallery4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_decorate.*
import kotlinx.android.synthetic.main.activity_draw.*

class DrawActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)

        if(intent.hasExtra("uri")) {
            val uriString = this.intent.getStringExtra("uri")
            if (uriString != null) {
                val uri = Uri.parse(uriString)
                imagecanvas.setImageURI(uri)
            }
        }
    }
}

