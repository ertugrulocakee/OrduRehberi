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
import kotlinx.android.synthetic.main.activity_istasyon_olustur.*
import kotlinx.android.synthetic.main.activity_sanayi_olustur.*

class SanayiOlusturActivity : AppCompatActivity() {

    var sanayiAdi = ""
    var sanayiGorseli : Uri? = null
    var sanayiAlani =""
    var sanayiTelefon=""
    var sanayiWeb=""
    var sanayiAciklama=""


    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sanayi_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()



    }


    fun sanayiResmiSec(view: View){
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


    fun sanayiOlustur(view: View){

        sanayiGorseli = secilenGorsel
        sanayiAdi = sanayiAdiOlustur.text.toString()
        sanayiAlani = sanayiTuruOlustur.text.toString()
        sanayiTelefon=sanayiTelefonOlustur.text.toString()
        sanayiWeb=sanayiWebOlustur.text.toString()
        sanayiAciklama=sanayiAciklamaOlustur.text.toString()

        if (sanayiGorseli !=null && sanayiAdi!="" && sanayiTelefon !="" && sanayiAlani != "" && sanayiWeb != ""  && sanayiAciklama !=""){


            val intent = Intent(this, MapsActivity6::class.java)
            intent.putExtra("sanayiAdi",sanayiAdi)
            intent.putExtra("sanayiGorseli",sanayiGorseli.toString())
            intent.putExtra("sanayiAlani",sanayiAlani)
            intent.putExtra("sanayiTelefon",sanayiTelefon)
            intent.putExtra("sanayiWeb",sanayiWeb)
            intent.putExtra("sanayiAciklama",sanayiAciklama)
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
                    sanayiResmiOlustur.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    sanayiResmiOlustur.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }


}