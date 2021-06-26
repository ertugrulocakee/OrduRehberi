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
import com.ocak.adminapp.databinding.ActivityMaps6Binding
import java.util.*

class MapsActivity6 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps6Binding

    private  lateinit var  storage: FirebaseStorage
    private lateinit var  database : FirebaseFirestore

    var sanayi_Adi = ""
    var sanayi_Gorseli = ""
    var sanayi_Alani =""
    var sanayi_Telefon=""
    var sanayi_Web=""
    var sanayi_Aciklama=""
    var latitude =""
    var longitude=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps6Binding.inflate(layoutInflater)
        setContentView(binding.root)


        storage = FirebaseStorage.getInstance()
        database= FirebaseFirestore.getInstance()


        sanayi_Adi=intent.getStringExtra("sanayiAdi").toString()
        sanayi_Gorseli=intent.getStringExtra("sanayiGorseli").toString()
        sanayi_Alani=intent.getStringExtra("sanayiAlani").toString()
        sanayi_Telefon=intent.getStringExtra("sanayiTelefon").toString()
        sanayi_Web=intent.getStringExtra("sanayiWeb").toString()
        sanayi_Aciklama=intent.getStringExtra("sanayiAciklama").toString()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.sanayi_kaydet,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.sanayiKaydet){
            firmayiKaydetFirebase()
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
            mMap.addMarker(MarkerOptions().position(p0!!).title(sanayi_Adi))
            latitude = p0.latitude.toString()
            longitude =p0.longitude.toString()

            Toast.makeText(applicationContext,"Artık bu firmayı kayıt edebilirsin!", Toast.LENGTH_SHORT).show()

        }
    }

    fun firmayiKaydetFirebase(){


        val uuid = UUID.randomUUID()
        val gorselIsmi = " ${uuid}.jpg"

        val reference = storage.reference
        val gorselReference = reference.child("sanayiGorselleri").child(gorselIsmi)
        val sanayiGorseliUri=sanayi_Gorseli.toUri()

        gorselReference.putFile(sanayiGorseliUri!!).addOnSuccessListener {
            val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("sanayiGorselleri").child(gorselIsmi)
            yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                val downloadUri = it.toString()

                val postHashMap = hashMapOf<String,Any>()
                postHashMap.put("sanayigorsel",downloadUri)
                postHashMap.put("sanayiad",sanayi_Adi)
                postHashMap.put("sanayialani",sanayi_Alani)
                postHashMap.put("sanayitelefon",sanayi_Telefon)
                postHashMap.put("latitude",latitude)
                postHashMap.put("longitude",longitude)
                postHashMap.put("sanayiWeb", sanayi_Web)
                postHashMap.put("sanayiaciklama",sanayi_Aciklama)


                database.collection("SanayiFirmalari").add(postHashMap).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(applicationContext,"Firma oluşturuldu.", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, SanayiListesiActivity::class.java)
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