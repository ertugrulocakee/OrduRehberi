package com.ocak.firebaseordu.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R

class HesapSilmeActivity() : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_name_delete_account)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    fun silHesabini(view: View){

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Sil")
        alert.setMessage("Hesabınızı silmek istediğinize emin misiniz?")
        alert.setCancelable(false);
        alert.setPositiveButton("Evet") { dialogInterface: DialogInterface, i: Int ->

            var user = auth.currentUser

            user.delete().addOnCompleteListener {
                if (it.isSuccessful) {

                    Toast.makeText(this, "Kullanıcı silindi!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, UygulamaGirisActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        alert.setNegativeButton("Hayır") { dialogInterface: DialogInterface, i: Int ->

        }
        alert.show()

    }



}