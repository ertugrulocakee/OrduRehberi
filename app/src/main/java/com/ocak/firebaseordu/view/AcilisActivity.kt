package com.ocak.firebaseordu.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ocak.firebaseordu.R

class AcilisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acilis)
        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this,UygulamaGirisActivity::class.java)
            startActivity(intent)
            finish()
        },3000)



    }
}