package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ocak.adminapp.R

class UygulamaAyarlariActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)


    }

    fun changeUserName(view: View){
        val intent = Intent( this , HesapSilmeActivity::class.java)
        startActivity(intent)
    }


    fun changeEmail(view: View){

        val intent = Intent( this , EpostaDegisimiActivity::class.java)
        startActivity(intent)

    }

    fun changePicture(view: View){

        val intent = Intent( this , KullaniciBilgileriActivity::class.java)
        startActivity(intent)


    }

    fun changePassword(view: View){

        val intent = Intent(this, SifreDegistirmeActivity::class.java)
        startActivity(intent)

    }


}