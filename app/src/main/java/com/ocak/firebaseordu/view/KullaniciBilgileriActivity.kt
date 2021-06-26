package com.ocak.firebaseordu.view

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.ocak.firebaseordu.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_change_picture.*

class KullaniciBilgileriActivity : AppCompatActivity() {


    var secilenBitmap : Bitmap? =null


    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_picture)


        auth = FirebaseAuth.getInstance()



        val guncelKullanici = auth.currentUser
        guncelKullanici?.let {
            for (profile in it.providerData) {

                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl


                kullaniciadigoster.text = name
                kullaniciemailgoster.text = "E-mail: ${email}"

                Picasso.get().load(photoUrl).into(profilresmigoster)





            }


        }

    }






}