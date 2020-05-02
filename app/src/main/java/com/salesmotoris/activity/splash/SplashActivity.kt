package com.salesmotoris.activity.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.salesmotoris.R
import com.salesmotoris.activity.home.HomeActivity
import com.salesmotoris.activity.login.LoginActivity
import com.salesmotoris.library.SalesMotorisPref
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash)

        Picasso.get().load(R.drawable.img_logo).into(imageview_splash_logo)
        if (SalesMotorisPref(this).accessToken == null){
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 2000)
        }else{
            Handler().postDelayed({
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, 2000)
        }
    }
}
