package com.ocak.adminapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ocak.adminapp.R
import kotlinx.android.synthetic.main.activity_muzik_ekle.*
import kotlinx.android.synthetic.main.activity_yemek_olustur.*
import java.util.*

class MuzikEkleActivity : AppCompatActivity() {

    var muzikUri : Uri? = null
    var muzikAdi =""
    var muzikTuru =""
    var muzikSeslendirenSanatci = ""

    private  lateinit var  storage : FirebaseStorage
    private lateinit var  database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muzik_ekle)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    fun muzikSec(view: View){


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("audio/*")
            startActivityForResult(intent,2)

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            muzikUri = data.data

            Toast.makeText(this,"Müzik seçildi",Toast.LENGTH_SHORT).show()

        }


        super.onActivityResult(requestCode, resultCode, data)

    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                val intent = getIntent()
                intent.setType("audio/*")
                intent.setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(intent,2)

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }




    fun muzikEkle(view:View){

        muzikAdi = parcaAdiOlustur.text.toString()
        muzikTuru = parcaTuruOlustur.text.toString()
        muzikSeslendirenSanatci= parcaSeslendirenOlustur.text.toString()



        if (muzikUri !=null && muzikAdi!="" && muzikTuru !="" && muzikSeslendirenSanatci != "" ){

            val uuid = UUID.randomUUID()
            val muzikIsmi = "${uuid}"

            val reference = storage.reference
            val muzikReference = reference.child("Muzikler").child(muzikIsmi)


            muzikReference.putFile(muzikUri!!).addOnSuccessListener {
                val yuklenenMuzikReference = FirebaseStorage.getInstance().reference.child("Muzikler").child(muzikIsmi)
                yuklenenMuzikReference.downloadUrl.addOnSuccessListener {

                    val downloadUri = it.toString()

                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("muzikdosyasi",downloadUri)
                    postHashMap.put("muzikadi", muzikAdi)
                    postHashMap.put("muzikturu", muzikTuru)
                    postHashMap.put("muzikseslendiren",muzikSeslendirenSanatci)


                    database.collection("Muzik").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(applicationContext,"Parça eklendi.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MuzikListesiActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
                    }


                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
                }



            }.addOnFailureListener {
                Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
            }



        }else{
            Toast.makeText(this,"Eksik işlem var!", Toast.LENGTH_LONG).show()
            return
        }





    }







}