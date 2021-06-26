package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.ocak.adminapp.R
import kotlinx.android.synthetic.main.activity_main.*

class UygulamaGirisActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        database = FirebaseFirestore.getInstance()


        val currentUser = auth.currentUser


        if(currentUser != null){
            if (currentUser.isEmailVerified == true){
                val intent = Intent(this, OrduDeneyimleriActivity::class.java)
                startActivity(intent)
                finish()

            }





        }

    }


    fun kayitOl(view: View){
        val intent = Intent(this, KayitIslemiActivity::class.java)
        startActivity(intent)

    }


    fun girisYap(view: View){


        val email = eMailText.text.toString()
        val sifre = sifreText.text.toString()

        if (sifre != "" && email != "") {


            auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener {
                if (it.isSuccessful) {
                    if(auth.currentUser.isEmailVerified == true){

                        val guncelKullanici = auth.currentUser!!.displayName

                        Toast.makeText(this, "Hoşgeldiniz ${guncelKullanici} !", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, OrduDeneyimleriActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else{
                        Toast.makeText(this,"Hesabın doğrulanmış değil.Lütfen doğrula", Toast.LENGTH_LONG).show()

                    }

                }


            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(applicationContext,"Boş kısım bırakmayın !", Toast.LENGTH_LONG).show()
        }


    }


    fun unutma(view: View) {


        if(eMailText.text.toString() == "" && sifreText.text.toString() == "" ){
            Toast.makeText(this,"E-mail adresinizi yazdıktan sonra bu butona tıklayınız! ", Toast.LENGTH_LONG).show()
            return
        }

        if (eMailText.text.toString() != null) {


            val guncelKullanici = auth
            guncelKullanici.sendPasswordResetEmail(eMailText.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Şifre sıfırlama e-postası gönderildi!", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this,"E-mail adresinizi yazdıktan sonra bu butona tıklayınız! ", Toast.LENGTH_LONG).show()

        }
    }





}