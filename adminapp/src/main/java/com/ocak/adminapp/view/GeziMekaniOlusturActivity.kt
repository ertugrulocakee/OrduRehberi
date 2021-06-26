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
import kotlinx.android.synthetic.main.activity_gezi_mekani_olustur.*

class GeziMekaniOlusturActivity : AppCompatActivity() {

    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null
    var geziMekaniAdi =""
    var geziMekaniTipi = ""
    var geziMekaniAciklama =""
    var geziMekaniGorseli : Uri? = null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gezi_mekani_olustur)


        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

    }


    fun gezi_mekani_resim_sec(view: View){

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

    fun geziMekaniOlustur(view: View){

        geziMekaniGorseli=secilenGorsel
        geziMekaniAdi=gezi_Mekani_Ekle_Ad.text.toString()
        geziMekaniAciklama=gezi_Mekani_Ekle_Aciklama.text.toString()
        geziMekaniTipi=gezi_Mekani_Ekle_Nitelik.text.toString()




        if (geziMekaniAdi != "" && geziMekaniGorseli != null && geziMekaniAciklama != "" && geziMekaniTipi != ""){

            val intent = Intent(this, MapsActivity2::class.java)
            intent.putExtra("mekanEkleGorsel",geziMekaniGorseli.toString())
            intent.putExtra("geziMekaniAdi",geziMekaniAdi)
            intent.putExtra("geziMekaniAciklama",geziMekaniAciklama)
            intent.putExtra("geziMekaniTipi",geziMekaniTipi)
            this.startActivity(intent)

        }else{

            Toast.makeText(this,"Eksik işlem var !",Toast.LENGTH_LONG).show()
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
                    gezi_mekani_ekle_resim.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    gezi_mekani_ekle_resim.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }






}