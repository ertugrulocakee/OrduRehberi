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
import java.util.*


class IstasyonOlusturActivity : AppCompatActivity() {

    var istasyonAdi =""
    var istasyonKapasite =""
    var istasyonGorseli : Uri? = null
    var faaliyetTarihi = ""
    var istasyonTipi =""
    var istasyonTelefonu=""
    var istasyonWeb=""



    var secilenGorsel: Uri? = null
    var secilenBitmap :Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_istasyon_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

         val takvim = Calendar.getInstance()

        val yil = takvim.get(Calendar.YEAR)
        val ay = takvim.get(Calendar.MONTH)
        val gun = takvim.get(Calendar.DATE)

        faaliyetgun.setOnClickListener {

            val dpd=DatePickerDialog(this,DatePickerDialog.OnDateSetListener { view, yil, ay, gun ->

                faaliyetTarihi=""+gun+"/"+ay+"/"+yil
            },yil,ay+1,gun)
            dpd.show()
        }

    }


    fun istasyonGorseliEkle(view: View){

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


    fun istasyonuEkle(view: View){

        istasyonGorseli = secilenGorsel
        istasyonAdi = istasyonadi.text.toString()
        istasyonKapasite = istasyon_kapasite.text.toString()
        istasyonTelefonu=istasyon_Telefonu.text.toString()
        istasyonTipi=istasyontipi.text.toString()
        istasyonWeb=istasyonOlusturWeb.text.toString()

        if (istasyonGorseli !=null && istasyonAdi!="" && istasyonTelefonu !="" &&faaliyetTarihi != "" && istasyonTipi != "" && istasyonKapasite != "" && istasyonWeb !=""){


            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("istasyonAdi",istasyonAdi)
            intent.putExtra("istasyonGorseli",istasyonGorseli.toString())
            intent.putExtra("istasyonKapasite",istasyonKapasite)
            intent.putExtra("istasyonTarihi",faaliyetTarihi)
            intent.putExtra("istasyonTelefonu",istasyonTelefonu)
            intent.putExtra("istasyonTipi",istasyonTipi)
            intent.putExtra("istasyonWeb",istasyonWeb)
            startActivity(intent)

        }else{
            Toast.makeText(this,"Eksik işlem var!",Toast.LENGTH_LONG).show()
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
                    istasyongorsel.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    istasyongorsel.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }










}