package com.ocak.firebaseordu.view

import android.Manifest
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
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ocak.firebaseordu.R
import kotlinx.android.synthetic.main.activity_share_post.*
import java.util.*

class OrduDeneyimPaylasActivity : AppCompatActivity() {

    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    var deneyimTuru=""



    private  lateinit var  storage: FirebaseStorage
    private lateinit var  auth: FirebaseAuth
    private lateinit var  database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_post)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()


    }

    fun resimSec(view: View){

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else{
            val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntent,2)

        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if(requestCode == 1){
            if(grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                val galeriIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntent,2)

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode==2 && resultCode== RESULT_OK && data != null){

            secilenGorsel = data.data

            if(secilenGorsel != null){

                if (Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    imageViewLokasyon.setImageBitmap(secilenBitmap)


                }else{
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    imageViewLokasyon.setImageBitmap(secilenBitmap)

                }



            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    fun paylas(view: View){


        val kullaniciYorumu = yorumText.text.toString()
        val kullaniciPuani = ratingBar.rating.toString()


        if (kullaniciPuani.isEmpty() == false && kullaniciYorumu.isEmpty() == false && secilenGorsel != null && deneyimTuru!="") {


            val uuid = UUID.randomUUID()
            val gorselIsmi = " ${uuid}.jpg"

            val reference = storage.reference
            val gorselReference = reference.child("images").child(gorselIsmi)


            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener {
                val yuklenenGorselinReference =
                    FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                    val downloadUri = it.toString()
                    val tarih = Timestamp.now()
                    val kullaniciadi = auth.currentUser!!.displayName
                    val kullaniciGorseli = auth.currentUser?.photoUrl.toString()
                    val kullaniciUid = auth.currentUser?.uid.toString()

                    var check = ""

                    if (checkbox.isChecked) {

                        check = "Bu deneyimi tekrar yaşamak isterim."
                    } else {
                        check = "Bu deneyimi tekrar yaşamak istemem."
                    }


                    val postHashMap = hashMapOf<String, Any>()
                    postHashMap.put("gorselurl", downloadUri)
                    postHashMap.put("tarih", tarih)
                    postHashMap.put("puan", kullaniciPuani)
                    postHashMap.put("yorum", kullaniciYorumu)
                    postHashMap.put("kullaniciadi", kullaniciadi!!)
                    postHashMap.put("kullaniciresmi", kullaniciGorseli)
                    postHashMap.put("onaykutusu", check)
                    postHashMap.put("uid", kullaniciUid)
                    postHashMap.put("deneyimTuru",deneyimTuru)




                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Paylaşım başarılı",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            applicationContext,
                            it.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }




                }.addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                }


            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }


        } else {
            Toast.makeText(applicationContext, "Eksik işleminiz var!", Toast.LENGTH_LONG).show()
        }



    }

    fun sec(view: View){

        val radioId = deneyimAlani.checkedRadioButtonId

        if (radioId == R.id.geziKitabiButonu){

            deneyimTuru="Gezi Kitabı"

        }

        if (radioId== R.id.restorantButonu){

            deneyimTuru="Restorant"

        }

        if (radioId == R.id.otelButonu){

            deneyimTuru="Otel"

        }


        if (radioId==R.id.istasyonButonu){

            deneyimTuru="İstasyon"
        }



    }



}