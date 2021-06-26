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
import kotlinx.android.synthetic.main.activity_tarim_urunu_olustur.*
import java.util.*

class TarimUrunuOlusturActivity : AppCompatActivity() {

    var tarimUrunuTuru = ""
    var tarimUrunuGorseli : Uri? = null
    var tarimUrunuBakimi =""

    var secilenGorsel: Uri? = null
    var secilenBitmap : Bitmap?= null


    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarim_urunu_olustur)

        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()



    }

    fun tarimUrunuResmiSec(view: View){

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

    fun tarimUrunuOlustur(view: View){


        tarimUrunuGorseli = secilenGorsel
        tarimUrunuTuru = tarimTuruOlustur.text.toString()
        tarimUrunuBakimi= tarimBakimiOlustur.text.toString()



        if (tarimUrunuGorseli !=null &&  tarimUrunuTuru !="" && tarimUrunuBakimi != ""){


            val uuid = UUID.randomUUID()
            val gorselIsmi = " ${uuid}.jpg"

            val reference = storage.reference
            val gorselReference = reference.child("tarimurungorselleri").child(gorselIsmi)

            val tarimUrunuGorseliUri=tarimUrunuGorseli

            gorselReference.putFile(tarimUrunuGorseliUri!!).addOnSuccessListener {
                val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("tarimurungorselleri").child(gorselIsmi)
                yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                    val downloadUri = it.toString()

                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("tarimurunugorseli",downloadUri)
                    postHashMap.put("tarimurunuturu", tarimUrunuTuru)
                    postHashMap.put("tarimurunubakimi",tarimUrunuBakimi)



                    database.collection("TarimUrunleri").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(applicationContext,"Tarım Ürünü oluşturuldu.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, TarimUrunuListesiActivity::class.java)
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
                    tarimUrunuResmiOlustur.setImageBitmap(secilenBitmap)


                } else {
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, secilenGorsel)
                    tarimUrunuResmiOlustur.setImageBitmap(secilenBitmap)
                }

            }

        }


        super.onActivityResult(requestCode, resultCode, data)
    }





}