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
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ocak.adminapp.R
import kotlinx.android.synthetic.main.activity_egitim_olustur.*


class OkulOlusturActivity : AppCompatActivity() {



    var okulAdi = ""
    var okulGorseli : Uri? = null
    var okulSeviyesi =""
    var okulTuru=""
    var okulWeb=""
    var okulTelefon=""
    var okulAciklama=""


    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egitim_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()


    }

    fun egitimResmiSec(view: View){

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

    fun okulOlustur(view:View){


        okulGorseli = secilenGorsel
        okulAdi = okulAdiOlustur.text.toString()
        okulTelefon= okulTelefonOlustur.text.toString()
        okulWeb= okulWebOlustur.text.toString()
        okulAciklama= okulAciklamaOlustur.text.toString()

        if (okulGorseli !=null && okulAdi!="" && okulTelefon !="" && okulSeviyesi != "" && okulWeb != ""  && okulAciklama !="" && okulTuru !=""){


            val intent = Intent(this, MapsActivity8::class.java)
            intent.putExtra("okulAdi",okulAdi)
            intent.putExtra("okulGorseli",okulGorseli.toString())
            intent.putExtra("okulSeviyesi",okulSeviyesi)
            intent.putExtra("okulTelefon",okulTelefon)
            intent.putExtra("okulWeb",okulWeb)
            intent.putExtra("okulAciklama",okulAciklama)
            intent.putExtra("okulTuru",okulTuru)
            startActivity(intent)

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
                    egitimResmiOlustur.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    egitimResmiOlustur.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }


    fun okulTuruSec(view: View){

        val id = okulturuAlani.checkedRadioButtonId

        if(id == R.id.ilkokulButonu){

            okulSeviyesi = "İlkokul"

        }

        if (id==R.id.ortaokulButonu){

            okulSeviyesi= "Ortaokul"

        }

        if (id==R.id.liseButonu){

            okulSeviyesi= "Lise"
        }

        if (id == R.id.universiteButonu){

            okulSeviyesi = "Üniversite"

        }


    }


    fun okulSec(view: View){

        val id = devletOzelAlani.checkedRadioButtonId

        if ( id == R.id.devletButonu){

            okulTuru = "Devlet Okulu"

        }

        if (id == R.id.ozelButonu){

            okulTuru = "Özel Okul"

        }



    }



}