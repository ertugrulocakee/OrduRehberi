package com.ocak.adminapp.view

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ocak.adminapp.R
import kotlinx.android.synthetic.main.activity_change_password.*

class SifreDegistirmeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth = FirebaseAuth.getInstance()

    }



    fun sifreDegistir(view: View){

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Şifre")
        alert.setMessage("Şifreyi değiştirmek istediğinize emin misiniz?")
        alert.setCancelable(false);


        alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->

            val sifrebir = editNewPasswordText.text.toString()
            val sifreiki = editNewPasswordTextTwo.text.toString()

            if (sifreiki.equals(sifrebir) == false) {
                Toast.makeText(this, "Lütfen dikkatli şifre gir!", Toast.LENGTH_LONG).show()
                return@setPositiveButton
            }

            val guncelKullanici = auth.currentUser


            if (guncelKullanici != null) {

                guncelKullanici.updatePassword(sifrebir).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Şifreniz başarıyla güncellendi!", Toast.LENGTH_LONG)
                            .show()
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



}