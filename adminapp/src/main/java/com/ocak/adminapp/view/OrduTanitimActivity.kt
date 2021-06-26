package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ocak.adminapp.R


class OrduTanitimActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
    }


    fun yemek(view: View){

        val intent = Intent(this,YemekListesiActivity::class.java)
        this.startActivity(intent)

    }

    fun music(view: View){

        val intent = Intent(this,MuzikListesiActivity::class.java)
        this.startActivity(intent)
    }

    fun farm(view: View){

        val intent = Intent(this,HayvanIsiListesiActivity::class.java)
        this.startActivity(intent)

    }

    fun tarim(view: View){

        val intent = Intent(this,TarimUrunuListesiActivity::class.java)
        this.startActivity(intent)

    }

    fun medya(view: View){

            val intent = Intent(this,MedyaListesiActivity::class.java)
            this.startActivity(intent)

    }

    fun hikaye(view: View){

        val intent = Intent(this,UlasimActivity::class.java)
        this.startActivity(intent)


    }

    fun sport(view: View){

        val intent = Intent(this,SporListesiActivity::class.java)
        this.startActivity(intent)



    }

    fun yetkili(view: View){

        val intent = Intent(this,YetkiliListesiActivity::class.java)
        this.startActivity(intent)

    }

    fun unlu (view: View){

        val intent = Intent(this,SanayiListesiActivity::class.java)
        this.startActivity(intent)

    }

    fun education(view: View){

        val intent = Intent(this,OkulListesiActivity::class.java)
        this.startActivity(intent)


    }

    fun art(view: View){

        val intent = Intent(this,EtkinlikListesiActivity::class.java)
        this.startActivity(intent)

    }

    fun hava(view: View){

        val intent = Intent(this,HavaActivity::class.java)
        this.startActivity(intent)

    }

}