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
import kotlinx.android.synthetic.main.activity_otel_olustur.*

class OtelOlusturActivity : AppCompatActivity() {

    var otelAdi=""
    var otelGorseli:Uri? =null
    var otelYildizSayisi=""
    var otelWeb=""
    var otelTelefon=""
    var otelAciklama=""


    var wifiBilgisi=""
    var restorantBilgisi=""
    var barBilgisi=""
    var acikBufeKahvaltiBilgisi=""
    var spaMasajBilgisi=""
    var sporSalonuBilgisi=""
    var saunaBilgisi=""
    var parkAlaniBilgisi=""
    var engelliBilgisi=""
    var cocukBilgisi=""
    var evcilHayvanBilgisi=""
    var toplantiOdasiBilgisi=""
    var camasirhaneBilgisi=""
    var havuzBilgisi=""




    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otel_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()


    }


    fun otelGorseliEkle(view: View){

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
                    otelOlusturResim.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    otelOlusturResim.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }


    fun otelEkle(view: View){

       otelAdi=otelOlusturAd.text.toString()
       otelAciklama=otelOlusturAciklama.text.toString()
       otelTelefon=otelOlusturTelefon.text.toString()
       otelGorseli=secilenGorsel
       otelWeb=otelOlusturWeb.text.toString()
       otelYildizSayisi=otelOlusturYildiz.rating.toInt().toString()

       if (otelOlusturAcikBufeKahvalti.isChecked){
         acikBufeKahvaltiBilgisi="Otelde açık büfe kahvaltı hizmeti bulunmaktadır."
       }else{
           acikBufeKahvaltiBilgisi="Otelde açık büfe kahvaltı hizmeti bulunmamaktadır."
       }

       if (otelOlusturCamasirhane.isChecked){
           camasirhaneBilgisi="Otelde çamaşırhane bulunmaktadır."
       }else{
           camasirhaneBilgisi="Otelde çamaşırhane bulunmamaktadır."
       }

       if (otelOlusturBar.isChecked){
           barBilgisi="Otelde bar bulunmaktadır."
       }else{
           barBilgisi="Otelde bar bulunmamaktadır."
       }

       if (otelOlusturHavuz.isChecked){
           havuzBilgisi="Otelde havuz bulunmaktadır."
       }else{
           havuzBilgisi="Otelde havuz bulunmamaktadır."
       }

      if(otelOlusturParkAlani.isChecked){
          parkAlaniBilgisi="Otelde araç park alanı bulunmaktadır."
      }else{
          parkAlaniBilgisi="Otelde araç park alanı bulunmamaktadır."
      }


      if (otelOlusturCocukUygunluk.isChecked){
          cocukBilgisi="Otel çocuk bireyler için uygundur."

      }else{
          cocukBilgisi="Otel çocuk bireyler için uygun değildir."
      }

      if (otelOlusturEngelliUygunluk.isChecked){
          engelliBilgisi="Otel engelli bireyler için uygundur."
      }else{
          engelliBilgisi="Otel engelli bireyler için uygun değildir."
      }

      if(otelOlusturEvcilHayvanUygunluk.isChecked){
          evcilHayvanBilgisi="Otel evcil hayvanlar için uygundur."
      }else{
          evcilHayvanBilgisi="Otel evcil hayvanlar için uygun değildir."
      }

     if (otelOlusturRestorant.isChecked){
         restorantBilgisi="Otelde restorant bulunmaktadır."
     }else{
         restorantBilgisi="Otelde restorant bulunmamaktadır."
     }

     if (otelOlusturSauna.isChecked){
         saunaBilgisi="Otelde sauna bulunmaktadır."
     }else{
         saunaBilgisi="Otelde sauna bulunmamaktadır."
     }

     if (otelOlusturSpaMasaj.isChecked){
         spaMasajBilgisi="Otelde spa ve masaj hizmeti bulunmaktadır."
     }else{
         spaMasajBilgisi="Otelde spa ve masaj hizmeti bulunmamaktadır."
     }

     if (otelOlusturSporSalonu.isChecked){
         sporSalonuBilgisi="Otelde spor salonu bulunmaktadır."
     }else{
         sporSalonuBilgisi="Otelde spor salonu bulunmamaktadır."
     }

     if (otelOlusturToplantiOdasi.isChecked){
         toplantiOdasiBilgisi="Otelde toplantı odaları bulunmaktadır."
     }else{
         toplantiOdasiBilgisi="Otelde toplantı odası bulunmamaktadır."
     }

     if (otelOlusturWifi.isChecked){
         wifiBilgisi="Otelde Wi-Fi hizmeti bulunmaktadır."
     }else{
         wifiBilgisi="Otelde Wi-Fi hizmeti bulunmamaktadır."
     }




   if (otelAciklama!="" && otelAdi!="" && otelGorseli!=null  &&otelTelefon!="" &&otelWeb!="" && otelYildizSayisi!=""){


       val intent = Intent(this,MapsActivity4::class.java)
       intent.putExtra("oteladi",otelAdi)
       intent.putExtra("otelGorseli",otelGorseli.toString())
       intent.putExtra("otelaciklama",otelAciklama)
       intent.putExtra("oteltelefon",otelTelefon)
       intent.putExtra("otelweb",otelWeb)
       intent.putExtra("otelyildiz",otelYildizSayisi)
       intent.putExtra("otelhavuz",havuzBilgisi)
       intent.putExtra("otelacikbufekahvalti",acikBufeKahvaltiBilgisi)
       intent.putExtra("otelcamasirhane",camasirhaneBilgisi)
       intent.putExtra("otelbar",barBilgisi)
       intent.putExtra("otelpark",parkAlaniBilgisi)
       intent.putExtra("otelcocuk",cocukBilgisi)
       intent.putExtra("otelengelli",engelliBilgisi)
       intent.putExtra("otelevcilhayvan",evcilHayvanBilgisi)
       intent.putExtra("otelrestorant",restorantBilgisi)
       intent.putExtra("otelsauna",saunaBilgisi)
       intent.putExtra("otelspamasaj",spaMasajBilgisi)
       intent.putExtra("otelsporsalonu",sporSalonuBilgisi)
       intent.putExtra("oteltoplantiodasi",toplantiOdasiBilgisi)
       intent.putExtra("otelwifi",wifiBilgisi)
       startActivity(intent)




   }else{

       Toast.makeText(this,"Eksik işlem var!",Toast.LENGTH_LONG).show()
       return
   }


    }







}