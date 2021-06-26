package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ocak.adminapp.R


class OrduSeyahatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lokasyonlar)
    }


    fun gezirehberi(view: View){

        val intent = Intent(this, GeziKitabiActivity::class.java)
        startActivity(intent)

    }


    fun ulasim(view: View){

        val intent = Intent(this, IstasyonListesiActivity::class.java)
        startActivity(intent)

    }

    fun otel(view: View){

        val intent = Intent(this, OtelListesiActivity::class.java)
        startActivity(intent)

    }

    fun restorant(view: View){


        val intent = Intent(this,RestorantListesiActivity::class.java)
        startActivity(intent)

    }

}