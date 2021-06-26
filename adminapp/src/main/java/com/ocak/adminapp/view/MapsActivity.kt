package com.ocak.adminapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var latitude =""
    var longitude =""
    var istasyonAdi =""
    var istasyonGorseli=""
    var istasyonKapasite=""
    var istasyonTelefonu=""
    var istasyonTipi=""
    var istasyonFaaliyet=""
    var istasyonWeb=""

    private  lateinit var  storage: FirebaseStorage
    private lateinit var  database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        storage = FirebaseStorage.getInstance()
        database= FirebaseFirestore.getInstance()

        istasyonAdi=intent.getStringExtra("istasyonAdi").toString()
        istasyonGorseli=intent.getStringExtra("istasyonGorseli").toString()
        istasyonKapasite=intent.getStringExtra("istasyonKapasite").toString()
        istasyonTelefonu=intent.getStringExtra("istasyonTelefonu").toString()
        istasyonTipi=intent.getStringExtra("istasyonTipi").toString()
        istasyonFaaliyet=intent.getStringExtra("istasyonTarihi").toString()
        istasyonWeb=intent.getStringExtra("istasyonWeb").toString()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.save_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.savestation){
            kaydetFirebase()
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
            mMap.addMarker(MarkerOptions().position(p0!!).title(istasyonAdi))
            latitude = p0.latitude.toString()
            longitude =p0.longitude.toString()

           Toast.makeText(applicationContext,"Artık bu istasyonu kayıt edebilirsin!",Toast.LENGTH_SHORT).show()

        }
    }


    fun kaydetFirebase(){


        val uuid = UUID.randomUUID()
        val gorselIsmi = " ${uuid}.jpg"

        val reference = storage.reference
        val gorselReference = reference.child("stationimages").child(gorselIsmi)
        val istasyonGorseliUri=istasyonGorseli.toUri()

        gorselReference.putFile(istasyonGorseliUri!!).addOnSuccessListener {
            val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("stationimages").child(gorselIsmi)
            yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                val downloadUri = it.toString()

                val postHashMap = hashMapOf<String,Any>()
                postHashMap.put("gorselstation",downloadUri)
                postHashMap.put("stationad", istasyonAdi)
                postHashMap.put("stationkapasite", istasyonKapasite)
                postHashMap.put("faaliyettarihi",istasyonFaaliyet)
                postHashMap.put("latitude",latitude)
                postHashMap.put("longitude",longitude)
                postHashMap.put("istasyonTipi", istasyonTipi)
                postHashMap.put("istasyonTelefonu", istasyonTelefonu)
                postHashMap.put("istasyonWeb",istasyonWeb)

                database.collection("Stations").add(postHashMap).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(applicationContext,"İstasyon oluşturuldu.", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, IstasyonListesiActivity::class.java)
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