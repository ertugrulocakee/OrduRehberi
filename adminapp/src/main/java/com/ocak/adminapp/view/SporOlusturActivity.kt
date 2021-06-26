package com.ocak.adminapp.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import kotlinx.android.synthetic.main.activity_spor_olustur.*
import kotlinx.android.synthetic.main.activity_yemek_olustur.*
import java.util.*

class SporOlusturActivity : AppCompatActivity() {

    var sporTakimAdi = ""
    var sporTakimTuru = ""
    var sporTakimGorseli : Uri? = null
    var sporTakimAlani =""
    var sporTakimAciklama = ""


    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spor_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

    }

    fun sporResmiSec(view: View){

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
            val galeriIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent, 2)

        }



    }


    fun sporOlustur(view: View){

        sporTakimGorseli = secilenGorsel
        sporTakimAdi = sporAdiOlustur.text.toString()
        sporTakimAlani= sporTuruOlustur.text.toString()
        sporTakimAciklama = sporAciklamaOlustur.text.toString()


        if (sporTakimGorseli !=null && sporTakimAdi!="" && sporTakimTuru !="" && sporTakimAlani != "" && sporTakimAciklama != "" ){


            val uuid = UUID.randomUUID()
            val gorselIsmi = " ${uuid}.jpg"

            val reference = storage.reference
            val gorselReference = reference.child("sporgorselleri").child(gorselIsmi)
            val sporTakimGorseliUri=sporTakimGorseli

            gorselReference.putFile(sporTakimGorseliUri!!).addOnSuccessListener {
                val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("sporgorselleri").child(gorselIsmi)
                yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                    val downloadUri = it.toString()

                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("sporgorseli",downloadUri)
                    postHashMap.put("sporadi", sporTakimAdi)
                    postHashMap.put("sporturu", sporTakimTuru)
                    postHashMap.put("sporAlani",sporTakimAlani)
                    postHashMap.put("sporAciklama",sporTakimAciklama)


                    database.collection("Spor").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(applicationContext,"Sporcu/Takım oluşturuldu.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, SporListesiActivity::class.java)
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 2)

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorsel = data.data

            if (secilenGorsel != null) {
                if (Build.VERSION.SDK_INT >= 28) {

                    val source = ImageDecoder.createSource(this.contentResolver, secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    sporResmiOlustur.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    sporResmiOlustur.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }

   fun bireyselTakimSec(view: View){

       val id = sporAlani.checkedRadioButtonId

       if (id==R.id.bireyselButonu){
           sporTakimTuru="Bireysel"

       }

       if (id==R.id.takimButonu){

           sporTakimTuru="Takım"

       }

   }





}