package com.ocak.adminapp.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ocak.adminapp.R
import com.ocak.adminapp.databinding.ActivityMaps8Binding
import java.util.*

class MapsActivity8 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps8Binding

    private  lateinit var  storage: FirebaseStorage
    private lateinit var  database : FirebaseFirestore

    var okul_Adi = ""
    var okul_Gorseli = ""
    var okul_Seviyesi =""
    var okul_Turu=""
    var okul_Aciklama=""
    var okul_Web=""
    var okul_Telefon =""
    var latitude =""
    var longitude=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps8Binding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        database= FirebaseFirestore.getInstance()


        okul_Adi=intent.getStringExtra("okulAdi").toString()
        okul_Gorseli=intent.getStringExtra("okulGorseli").toString()
        okul_Seviyesi=intent.getStringExtra("okulSeviyesi").toString()
        okul_Turu=intent.getStringExtra("okulTuru").toString()
        okul_Aciklama=intent.getStringExtra("okulAciklama").toString()
        okul_Telefon=intent.getStringExtra("okulTelefon").toString()
        okul_Web = intent.getStringExtra("okulWeb").toString()



        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.okulu_kaydet,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.okuluKaydet){
            okuluKaydetFirebase()
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(mylistener)

        val ordu = LatLng(40.9862, 37.8797)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(ordu).title("Ordu"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ordu,17f))

    }

    val mylistener = object : GoogleMap.OnMapLongClickListener{

        override fun onMapLongClick(p0: LatLng?) {

            mMap.clear()
            mMap.addMarker(MarkerOptions().position(p0!!).title(okul_Adi))
            latitude = p0.latitude.toString()
            longitude =p0.longitude.toString()

            Toast.makeText(applicationContext,"Artık bu okulu kayıt edebilirsin!", Toast.LENGTH_SHORT).show()

        }
    }


    fun okuluKaydetFirebase(){


        val uuid = UUID.randomUUID()
        val gorselIsmi = " ${uuid}.jpg"

        val reference = storage.reference
        val gorselReference = reference.child("okulGorselleri").child(gorselIsmi)
        val okulGorseliUri= okul_Gorseli.toUri()

        gorselReference.putFile(okulGorseliUri!!).addOnSuccessListener {
            val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("okulGorselleri").child(gorselIsmi)
            yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                val downloadUri = it.toString()

                val postHashMap = hashMapOf<String,Any>()
                postHashMap.put("okulgorsel",downloadUri)
                postHashMap.put("okulad",okul_Adi)
                postHashMap.put("okulseviyesi",okul_Seviyesi)
                postHashMap.put("okulturu",okul_Turu)
                postHashMap.put("latitude",latitude)
                postHashMap.put("longitude",longitude)
                postHashMap.put("okulaciklama",okul_Aciklama)
                postHashMap.put("okultelefon",okul_Telefon)
                postHashMap.put("okulweb",okul_Web)


                database.collection("Okullar").add(postHashMap).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(applicationContext,"Okul oluşturuldu.", Toast.LENGTH_LONG).show()
                        val intent = Intent(this,OkulListesiActivity::class.java)
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



    }



}