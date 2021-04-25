package com.privatememo.j.utility

import android.graphics.Point
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.privatememo.j.R
import kotlinx.android.synthetic.main.wholeimageactivity.*
import java.util.*

class WholeImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wholeimageactivity)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val width = size.x
        val height = size.y

        val getintent = getIntent()
        var uri = getintent.getStringExtra("imageUri")
        Glide.with(this).load(uri+ "? ${Date().getTime()}").override(width,height).error(R.drawable.ic_baseline_block_24).into(image)
    }
}