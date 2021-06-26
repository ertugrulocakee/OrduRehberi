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
import androidx.core.view.isVisible
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ocak.adminapp.R
import kotlinx.android.synthetic.main.activity_medya_olustur.*
import kotlinx.android.synthetic.main.activity_share_post.*
import kotlinx.android.synthetic.main.activity_yemek_olustur.*
import java.util.*

class MedyaOlusturActivity : AppCompatActivity() {

    var medyaAdi = ""
    var medyaTuru = ""
    var medyaGorseli : Uri? = null
    var medyaFrekans =""
    var medyaAciklama = ""

    var fec=""
    var polarizasyon =""
    var tvFrekans=""
    var tvSR =""

    var radyoFrekans =""
    var medyaWeb=""

    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medya_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

        televizyonAlani.visibility = View.GONE
        radyofrekansOlustur.visibility = View.GONE



    }



    fun medyaOlustur(view: View){

        medyaGorseli = secilenGorsel
        medyaAdi = medyaAdiOlustur.text.toString()
        medyaAciklama= medyaAciklamaOlustur.text.toString()
        medyaWeb = internetWebSitesiOlustur.text.toString()



        if (medyaGorseli !=null && medyaAdi!="" && medyaTuru !="" && medyaWeb != "" && medyaAciklama != "" ){


            if (medyaTuru=="Televizyon"){

                tvFrekans=tvfrekansOlustur.text.toString()
                tvSR = tvSrOlustur.text.toString()

                if (tvFrekans != "" && tvSR != "" && fec != "" && polarizasyon!=""){

                   medyaFrekans="Frekans:"+" "+tvFrekans+" "+"SR:"+" "+tvSR+" "+fec+" "+polarizasyon

                }else{
                  Toast.makeText(this,  "Eksik işlem var!",Toast.LENGTH_SHORT).show()
                    return
                }

            }

            if (medyaTuru=="Radyo"){

                radyoFrekans=radyofrekansOlustur.text.toString()

                if (radyoFrekans!=""){

                   medyaFrekans = "Frekans:"+" "+radyoFrekans

                }else{
                    Toast.makeText(this,  "Eksik işlem var!",Toast.LENGTH_SHORT).show()
                    return
                }



            }


            val uuid = UUID.randomUUID()
            val gorselIsmi = " ${uuid}.jpg"

            val reference = storage.reference
            val gorselReference = reference.child("medyagorselleri").child(gorselIsmi)
            val medyaGorseliUri=medyaGorseli

            gorselReference.putFile(medyaGorseliUri!!).addOnSuccessListener {
                val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("medyagorselleri").child(gorselIsmi)
                yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                    val downloadUri = it.toString()

                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("medyagorseli",downloadUri)
                    postHashMap.put("medyaadi", medyaAdi)
                    postHashMap.put("medyaturu", medyaTuru)
                    postHashMap.put("medyaaciklama",medyaAciklama)
                    postHashMap.put("medyaweb",medyaWeb)
                    postHashMap.put("medyafrekans",medyaFrekans)


                    database.collection("Medyalar").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(applicationContext,"Medya oluşturuldu.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MedyaListesiActivity::class.java)
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


    fun medyaResmiSec(view: View){

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
                    medyaResmiOlustur.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    medyaResmiOlustur.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }


    fun medyaTuruSec (view: View){

        val radioId = medyaAlani.checkedRadioButtonId

        if (radioId == R.id.televizyonButonu){


            medyaTuru="Televizyon"

            televizyonAlani.visibility = View.VISIBLE
            radyofrekansOlustur.visibility = View.GONE


        }

        if (radioId== R.id.radyoButonu){

            medyaTuru="Radyo"

            radyofrekansOlustur.visibility = View.VISIBLE
            televizyonAlani.visibility=View.GONE


        }

        if (radioId == R.id.gazeteButonu){

            medyaTuru="Gazete"

            radyofrekansOlustur.visibility = View.GONE
            televizyonAlani.visibility = View.GONE

        }


        if (radioId==R.id.internetButonu){

            medyaTuru="İnternet"

            radyofrekansOlustur.visibility = View.GONE
            televizyonAlani.visibility = View.GONE

        }


    }



    fun fecSec (view: View){

        val radioId = fecAlani.checkedRadioButtonId

        if (radioId == R.id.fec1){

            fec="FEC:2/3"

        }

        if (radioId== R.id.fec2){

            fec="FEC:3/4"

        }

        if (radioId == R.id.fec3){

            fec="FEC:5/6"


        }


    }



    fun vhSec(view: View){


        val radioId = vhAlani.checkedRadioButtonId

        if (radioId == R.id.vh1){

            polarizasyon="V"

        }

        if (radioId== R.id.vh2){

            polarizasyon="H"

        }

    }





}