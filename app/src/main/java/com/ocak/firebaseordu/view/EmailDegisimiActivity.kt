package com.ocak.firebaseordu.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ocak.firebaseordu.R
import kotlinx.android.synthetic.main.activity_new_email.*


class EmailDegisimiActivity : AppCompatActivity() {

    private  lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_email)

        auth = FirebaseAuth.getInstance()


    }


    fun degistirEmailini(view: View){


        val alert = AlertDialog.Builder(this)
        alert.setTitle("E-mail")
        alert.setMessage("E-mail adresinizi güncellemek istediğinize emin misiniz?")
        alert.setCancelable(false);
        alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->

            val guncelEmail = editNewEmailText.text.toString()

            val guncelKullanici = auth.currentUser
            if (guncelKullanici != null) {


                guncelKullanici.updateEmail(guncelEmail).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this,
                            "E-mail adresiniz başarıyla güncellendi!",
                            Toast.LENGTH_LONG
                        ).show()
                        yeniHesabiDogrula()
                        auth.signOut()
                        Toast.makeText(
                            applicationContext,
                            "Uygulamadan çıkıldı!",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this, UygulamaGirisActivity::class.java)
                        startActivity(intent)
                        finish()

                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }


            }
        }
        alert.setNegativeButton("Hayır") { dialogInterface: DialogInterface, i: Int ->

        }

        alert.show()

    }






    fun yeniHesabiDogrula(){

        auth.currentUser!!.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this,"Yeni e-postanız için doğrulama gönderildi!", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }



}


