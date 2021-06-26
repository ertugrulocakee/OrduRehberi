package com.ocak.adminapp.view

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
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
import kotlinx.android.synthetic.main.activity_kulturel_sanat_olustur.*
import kotlinx.android.synthetic.main.activity_sanayi_olustur.*
import java.util.*

class EtkinlikOlusturActivity : AppCompatActivity() {

    var etkinlikAdi = ""
    var etkinlikGorseli : Uri? = null
    var etkinlikAlani =""
    var etkinlikAciklama=""

    var etkinlikBaslangicTarihi = ""
    var etkinlikBitisTarihi = ""


    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kulturel_sanat_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()


        val takvim = Calendar.getInstance()

        val yil = takvim.get(Calendar.YEAR)
        val ay = takvim.get(Calendar.MONTH)
        val gun = takvim.get(Calendar.DATE)

        etkinlikBaslangicTarihiOlusturButonu.setOnClickListener {

            val dpd= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, yil, ay, gun ->

                etkinlikBaslangicTarihi =""+gun+"/"+ay+"/"+yil

            },yil,ay+1,gun)
            dpd.show()
        }

        etkinlikBitisTarihiOlusturButonu.setOnClickListener {

            val dpd= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, yil, ay, gun ->

                etkinlikBitisTarihi =""+gun+"/"+ay+"/"+yil

            },yil,ay+1,gun)
            dpd.show()
        }


    }


    fun kulturelSanatResmiSec(view: View){

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


    fun etkinlikOlustur(view: View){


        etkinlikGorseli = secilenGorsel
        etkinlikAdi = kulturelSanatAdiOlustur.text.toString()
        etkinlikAlani = kulturelSanatTuruOlustur.text.toString()
        etkinlikAciklama= kulturelSanatAciklamaOlustur.text.toString()

        if (etkinlikGorseli !=null && etkinlikAdi!="" && etkinlikAlani !="" && etkinlikAciklama != "" && etkinlikBaslangicTarihi != ""  && etkinlikBitisTarihi !=""){


            val intent = Intent(this, MapsActivity7::class.java)
            intent.putExtra("etkinlikAdi",etkinlikAdi)
            intent.putExtra("etkinlikGorseli",etkinlikGorseli.toString())
            intent.putExtra("etkinlikAlani",etkinlikAlani)
            intent.putExtra("etkinlikBaslangic",etkinlikBaslangicTarihi)
            intent.putExtra("etkinlikBitis",etkinlikBitisTarihi)
            intent.putExtra("etkinlikAciklama",etkinlikAciklama)
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
                    kulturelSanatResmiOlustur.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    kulturelSanatResmiOlustur.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }





}