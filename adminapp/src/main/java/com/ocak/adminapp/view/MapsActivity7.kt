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
import com.ocak.adminapp.databinding.ActivityMaps7Binding
import java.util.*

class MapsActivity7 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps7Binding


    private  lateinit var  storage: FirebaseStorage
    private lateinit var  database : FirebaseFirestore

    var etkinlik_Adi = ""
    var etkinlik_Gorseli = ""
    var etkinlik_Alani =""
    var etkinlik_Baslangic=""
    var etkinlik_Bitis=""
    var etkinlik_Aciklama=""
    var latitude =""
    var longitude=""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps7Binding.inflate(layoutInflater)
        setContentView(binding.root)


        storage = FirebaseStorage.getInstance()
        database= FirebaseFirestore.getInstance()


        etkinlik_Adi=intent.getStringExtra("etkinlikAdi").toString()
        etkinlik_Gorseli=intent.getStringExtra("etkinlikGorseli").toString()
        etkinlik_Alani=intent.getStringExtra("etkinlikAlani").toString()
        etkinlik_Baslangic=intent.getStringExtra("etkinlikBaslangic").toString()
        etkinlik_Bitis=intent.getStringExtra("etkinlikBitis").toString()
        etkinlik_Aciklama=intent.getStringExtra("etkinlikAciklama").toString()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.etkinlik_kaydet,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.etkinlikKaydet){
            etkinligiKaydetFirebase()
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
            mMap.addMarker(MarkerOptions().position(p0!!).title(etkinlik_Adi))
            latitude = p0.latitude.toString()
            longitude =p0.longitude.toString()

            Toast.makeText(applicationContext,"Artık bu etkinliği kayıt edebilirsin!", Toast.LENGTH_SHORT).show()

        }
    }


    fun etkinligiKaydetFirebase(){


        val uuid = UUID.randomUUID()
        val gorselIsmi = " ${uuid}.jpg"

        val reference = storage.reference
        val gorselReference = reference.child("etkinlikGorselleri").child(gorselIsmi)
        val etkinlikGorseliUri=etkinlik_Gorseli.toUri()

        gorselReference.putFile(etkinlikGorseliUri!!).addOnSuccessListener {
            val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("etkinlikGorselleri").child(gorselIsmi)
            yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                val downloadUri = it.toString()

                val postHashMap = hashMapOf<String,Any>()
                postHashMap.put("etkinlikgorsel",downloadUri)
                postHashMap.put("etkinlikad",etkinlik_Adi)
                postHashMap.put("etkinlikalani",etkinlik_Alani)
                postHashMap.put("etkinlikbaslangic",etkinlik_Baslangic)
                postHashMap.put("latitude",latitude)
                postHashMap.put("longitude",longitude)
                postHashMap.put("etkinlikbitis",etkinlik_Bitis)
                postHashMap.put("etkinlikaciklama",etkinlik_Aciklama)


                database.collection("KulturSanatEtkinlikleri").add(postHashMap).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(applicationContext,"Etkinlik oluşturuldu.", Toast.LENGTH_LONG).show()
                        val intent = Intent(this,EtkinlikListesiActivity::class.java)
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