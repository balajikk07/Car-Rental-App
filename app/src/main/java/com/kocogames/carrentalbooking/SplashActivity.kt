package com.kocogames.carrentalbooking

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val carImageView: ImageView = findViewById<ImageView>(R.id.carImageView)
        val animator = ObjectAnimator.ofFloat(carImageView, "translationX", -1500f, 2000f)
        animator.duration = 4000
        animator.start()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 4000)
    }

}
