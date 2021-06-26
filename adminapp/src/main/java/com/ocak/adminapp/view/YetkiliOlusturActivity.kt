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

import kotlinx.android.synthetic.main.activity_yetkili_olustur.*
import java.util.*

class YetkiliOlusturActivity : AppCompatActivity() {

    var yetkiliAdi = ""
    var yetkiliUniversite = ""
    var yetkiliGorseli : Uri? = null
    var yetkiliBolum =""
    var yetkiliMakam = ""
    var yetkiliMemleket =""
    var yetkiliAciklama =""
    var yetkiliDogumTarihi =""
    var yetkiliGoreveBaslangicTarihi=""



    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yetkili_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()

        val takvim = Calendar.getInstance()

        val yil = takvim.get(Calendar.YEAR)
        val ay = takvim.get(Calendar.MONTH)
        val gun = takvim.get(Calendar.DATE)

        yetkiliDogumTarihiOlustur.setOnClickListener {

            val dpd= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, yil, ay, gun ->

                yetkiliDogumTarihi=""+gun+"/"+ay+"/"+yil
            },yil,ay+1,gun)
            dpd.show()
        }

        yetkiliGoreveBaslangicOlustur.setOnClickListener {

            val dpd= DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, yil, ay, gun ->

                yetkiliGoreveBaslangicTarihi=""+gun+"/"+ay+"/"+yil
            },yil,ay+1,gun)
            dpd.show()
        }



    }

    fun yetkiliResmiSec(view:View){


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

    fun yetkiliOlustur(view: View){

        yetkiliGorseli = secilenGorsel
        yetkiliAdi = yetkiliAdSoyadOlustur.text.toString()
        yetkiliUniversite = yetkiliUniversiteOlustur.text.toString()
        yetkiliBolum= yetkiliBolumOlustur.text.toString()
        yetkiliAciklama = yetkiliAciklamaOlustur.text.toString()
        yetkiliMakam = yetkiliMakamOlustur.text.toString()
        yetkiliMemleket = yetkiliNereliOlustur.text.toString()


        if (yetkiliGorseli !=null && yetkiliAdi!="" && yetkiliUniversite !="" && yetkiliBolum != "" && yetkiliAciklama != "" && yetkiliMakam !="" && yetkiliMemleket !="" && yetkiliGoreveBaslangicTarihi !="" && yetkiliDogumTarihi!=""){


            val uuid = UUID.randomUUID()
            val gorselIsmi = " ${uuid}.jpg"

            val reference = storage.reference
            val gorselReference = reference.child("yetkiligorselleri").child(gorselIsmi)
            val yetkiliGorseliUri=yetkiliGorseli

            gorselReference.putFile(yetkiliGorseliUri!!).addOnSuccessListener {
                val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("yetkiligorselleri").child(gorselIsmi)
                yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                    val downloadUri = it.toString()

                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("yetkiligorseli",downloadUri)
                    postHashMap.put("yetkiliadi", yetkiliAdi)
                    postHashMap.put("yetkiliuniversite", yetkiliUniversite)
                    postHashMap.put("yetkilibolum",yetkiliBolum)
                    postHashMap.put("yetkiliaciklama",yetkiliAciklama)
                    postHashMap.put("yetkilimakam",yetkiliMakam)
                    postHashMap.put("yetkilimemleket",yetkiliMemleket)
                    postHashMap.put("yetkilidogumtarihi",yetkiliDogumTarihi)
                    postHashMap.put("yetkiligorevbaslangic",yetkiliGoreveBaslangicTarihi)


                    database.collection("Yetkililer").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(applicationContext,"Yetkili oluşturuldu.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, YetkiliListesiActivity::class.java)
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
                    yetkiliResmiOlustur.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    yetkiliResmiOlustur.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }







}