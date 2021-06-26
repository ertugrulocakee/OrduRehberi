package com.ocak.adminapp.view

import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
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
import kotlinx.android.synthetic.main.activity_restorant_olustur.*
import java.util.*

class RestorantOlusturActivity : AppCompatActivity() {


    var restorantAdi =""
    var restorantTipi =""
    var restorantResmi : Uri? = null
    var menuResmi : Uri? = null
    var restorantTelefonu =""
    var ortalamaTutar =""

    var pazartesiCalismaSaatiBaslangic =""
    var pazartesiCalismaSaatiBitis=""
    var saliCalismaSaatiBaslangic = ""
    var saliCalismaSaatiBitis=""
    var carsambaCalismaSaatiBaslangic = ""
    var carsambaCalismaSaatiBitis=""
    var persembeCalismaSaatiBaslangic = ""
    var persembeCalismaSaatiBitis=""
    var cumaCalismaSaatiBaslangic = ""
    var cumaCalismaSaatiBitis=""
    var cumartesiCalismaSaatiBaslangic = ""
    var cumartesiCalismaSaatiBitis=""
    var pazarCalismaSaatiBaslangic = ""
    var pazarCalismaSaatiBitis=""

    var pazartesiSaati=""
    var saliSaati=""
    var carsambaSaati=""
    var persembeSaati=""
    var cumaSaati=""
    var cumartesiSaati=""
    var pazarSaati=""

    var wifiBilgisi=""
    var icMekanBilgisi=""
    var disMekanBilgisi=""
    var sigaraAlaniBilgisi=""
    var cocukAlaniBilgisi=""
    var yerindeServisBilgisi=""
    var paketServisBilgisi=""
    var gelAlBilgisi=""


    var secilenGorselRestorant: Uri? = null
    var secilenGorselMenu : Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restorant_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

    }

  fun resimSecRestorant (view: View){

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

    fun resimSecMenu(view: View){


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                3
            )
        } else {
            val galeriIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent, 4)

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

        if (requestCode == 3) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent, 4)

            }

        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorselRestorant = data.data

            if (secilenGorselRestorant != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver, secilenGorselRestorant!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    restorantGorseli.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorselRestorant)
                    restorantGorseli.setImageBitmap(secilenBitmap)
                }

            }

        }

        if (requestCode == 4 && resultCode == Activity.RESULT_OK && data != null) {
            secilenGorselMenu = data.data

            if (secilenGorselMenu != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver, secilenGorselMenu!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    restorantMenu.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorselMenu)
                    restorantMenu.setImageBitmap(secilenBitmap)
                }

            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }




    fun restorantOlustur (view: View){

        restorantAdi = restorantAdiOlustur.text.toString()
        restorantTipi = restorantTipiOlustur.text.toString()
        restorantResmi = secilenGorselRestorant
        menuResmi = secilenGorselMenu
        restorantTelefonu = restorantTelefonuOlustur.text.toString()
        ortalamaTutar = restorantTutariOlustur.text.toString()


       if( wifi.isChecked){
            wifiBilgisi = "Restorantımızda Wİ-Fİ hizmeti mevcuttur. "

        }else{
            wifiBilgisi = "Restorantımızda Wİ-Fİ hizmeti mevcut değildir."
        }

       if (icMekan.isChecked){
           icMekanBilgisi="Restorantımızda iç mekan bulunmaktadır."
       }else{
           icMekanBilgisi="Restorantımızda iç mekan bulunmamaktadır."
       }

      if (disMekan.isChecked){
          disMekanBilgisi="Restorantımızda dış mekan bulunmaktadır."

      }else{
          disMekanBilgisi="Restorantımızda dış mekan bulunmamaktadır."
      }

      if (sigaraAlani.isChecked){
          sigaraAlaniBilgisi="Restorantımızda sigara içme alanı bulunmaktadır."
      }else{
          sigaraAlaniBilgisi="Restorantımızda sigara içme alanı bulunmamaktadır."

      }

      if (cocukAlani.isChecked){
          cocukAlaniBilgisi="Restorantımızda çocuk alanı bulunmaktadır."
      }else{
          cocukAlaniBilgisi="Restorantımızda çocuk alanı bulunmamaktadır."
      }

      if(paketServis.isChecked){
          paketServisBilgisi = "Restorantımızda paket servis hizmeti bulunmaktadır."
      }else{
          paketServisBilgisi = "Restorantımızda paket servis hizmeti bulunmamaktadır."
      }

      if (yerindeServis.isChecked){
          yerindeServisBilgisi="Restorantımızda yerinde servis hizmeti bulunmaktadır."
      }else{
          yerindeServisBilgisi="Restorantımızda yerinde servis hizmeti bulunmamaktadır."

      }

      if (gelAl.isChecked){
          gelAlBilgisi="Restorantımızda gel-al hizmeti bulunmaktadır."
      }else{
          gelAlBilgisi="Restorantımızda  gel-al hizmeti bulunmamaktadır."
      }



      if (restorantAdi !="" && restorantTipi !="" && restorantResmi != null && menuResmi !=null && restorantTelefonu!="" && ortalamaTutar!=""){

          if (pazartesiCalismaSaatiBaslangic != "" && saliCalismaSaatiBaslangic != "" && carsambaCalismaSaatiBaslangic !="" && persembeCalismaSaatiBaslangic != "" && cumaCalismaSaatiBaslangic != "" && cumartesiCalismaSaatiBaslangic != "" && pazarCalismaSaatiBaslangic != ""){

              if (pazartesiCalismaSaatiBitis != "" && saliCalismaSaatiBitis != "" && carsambaCalismaSaatiBitis !="" && persembeCalismaSaatiBitis != "" && cumaCalismaSaatiBitis != "" && cumartesiCalismaSaatiBitis != "" && pazarCalismaSaatiBitis != ""){


                  pazartesiSaati="Başlangıç:"+pazartesiCalismaSaatiBaslangic+" "+"Bitiş:"+pazartesiCalismaSaatiBitis
                  saliSaati="Başlangıç:"+saliCalismaSaatiBaslangic+" "+"Bitiş:"+saliCalismaSaatiBitis
                  carsambaSaati="Başlangıç:"+carsambaCalismaSaatiBaslangic+" "+"Bitiş:"+carsambaCalismaSaatiBitis
                  persembeSaati="Başlangıç:"+persembeCalismaSaatiBaslangic+" "+"Bitiş:"+persembeCalismaSaatiBitis
                  cumaSaati="Başlangıç:"+cumaCalismaSaatiBaslangic+" "+"Bitiş:"+cumartesiCalismaSaatiBitis
                  cumartesiSaati="Başlangıç:"+cumartesiCalismaSaatiBaslangic+" "+"Bitiş:"+cumartesiCalismaSaatiBitis
                  pazarSaati="Başlangıç:"+pazarCalismaSaatiBaslangic+" "+"Bitiş:"+pazarCalismaSaatiBitis


                    val intent = Intent(this,MapsActivity3::class.java)
                    intent.putExtra("restoranAdi",restorantAdi)
                    intent.putExtra("restoranTipi",restorantTipi)
                    intent.putExtra("restoranResmi",restorantResmi.toString())
                    intent.putExtra("menuResmi",menuResmi.toString())
                    intent.putExtra("restorantTelefonu",restorantTelefonu)
                    intent.putExtra("ortalamaTutar",ortalamaTutar)
                    intent.putExtra("pazartesi",pazartesiSaati)
                    intent.putExtra("sali",saliSaati)
                    intent.putExtra("carsamba",carsambaSaati)
                    intent.putExtra("persembe",persembeSaati)
                    intent.putExtra("cuma",cumaSaati)
                    intent.putExtra("cumartesi",cumartesiSaati)
                    intent.putExtra("pazar",pazarSaati)
                    intent.putExtra("icmekan",icMekanBilgisi)
                    intent.putExtra("dismekan",disMekanBilgisi)
                    intent.putExtra("gelal",gelAlBilgisi)
                    intent.putExtra("paketservis",paketServisBilgisi)
                    intent.putExtra("yerindeservis",yerindeServisBilgisi)
                    intent.putExtra("sigaraalani",sigaraAlaniBilgisi)
                    intent.putExtra("cocukalani",cocukAlaniBilgisi)
                    intent.putExtra("wifi",wifiBilgisi)
                    startActivity(intent)


              }else{
                  Toast.makeText(this,"Eksik işlem var!",Toast.LENGTH_LONG).show()
                  return
              }


          }else{

              Toast.makeText(this,"Eksik işlem var!",Toast.LENGTH_LONG).show()
              return
          }


      }else{
          Toast.makeText(this,"Eksik işlem var!",Toast.LENGTH_LONG).show()
          return
      }



    }

   fun pazartesiGiris(view: View){

       val takvim = Calendar.getInstance()
       val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

           takvim.set(Calendar.HOUR_OF_DAY,saat)
           takvim.set(Calendar.MINUTE,dakika)

           pazartesiCalismaSaatiBaslangic = saat.toString()+":"+dakika.toString()


       }

       TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()

   }

   fun pazartesiCikis (view: View){

       val takvim = Calendar.getInstance()
       val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

           takvim.set(Calendar.HOUR_OF_DAY,saat)
           takvim.set(Calendar.MINUTE,dakika)

           pazartesiCalismaSaatiBitis = saat.toString()+":"+dakika.toString()

       }

       TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


   }

    fun pazartesiKapali(view: View){

        pazartesiCalismaSaatiBitis = "-"
        pazartesiCalismaSaatiBaslangic ="-"

    }


    fun saliGiris(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            saliCalismaSaatiBaslangic = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun saliCikis(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            saliCalismaSaatiBitis = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun saliKapali(view: View){

        saliCalismaSaatiBitis = "-"
        saliCalismaSaatiBaslangic ="-"

    }


    fun carsambaGiris(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            carsambaCalismaSaatiBaslangic = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun carsambaCikis(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            carsambaCalismaSaatiBitis = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()

    }

    fun carsambaKapali(view: View){

        carsambaCalismaSaatiBitis = "-"
        carsambaCalismaSaatiBaslangic ="-"

    }


    fun persembeGiris(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            persembeCalismaSaatiBaslangic = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun persembeCikis(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            persembeCalismaSaatiBitis = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun persembeKapali(view: View){


        persembeCalismaSaatiBitis = "-"
        persembeCalismaSaatiBaslangic ="-"

    }

    fun cumaGiris(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            cumaCalismaSaatiBaslangic = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun cumaCikis(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            cumaCalismaSaatiBitis = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun cumaKapali(view: View){

        cumaCalismaSaatiBitis = "-"
        cumaCalismaSaatiBaslangic ="-"

    }

    fun cumartesiGiris(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            cumartesiCalismaSaatiBaslangic = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun cumartesiCikis(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            cumartesiCalismaSaatiBitis = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()

    }

    fun cumartesiKapali(view: View){


        cumartesiCalismaSaatiBitis = "-"
        cumartesiCalismaSaatiBaslangic ="-"

    }

    fun pazarGiris(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            pazarCalismaSaatiBaslangic = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun pazarCikis(view: View){

        val takvim = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,saat,dakika ->

            takvim.set(Calendar.HOUR_OF_DAY,saat)
            takvim.set(Calendar.MINUTE,dakika)

            pazarCalismaSaatiBitis = saat.toString()+":"+dakika.toString()

        }

        TimePickerDialog(this,timeSetListener,takvim.get(Calendar.HOUR_OF_DAY),takvim.get(Calendar.MINUTE),true).show()


    }

    fun pazarKapali(view: View){


        pazarCalismaSaatiBitis = "-"
        pazarCalismaSaatiBaslangic ="-"


    }


}