package com.ocak.adminapp.view

import android.content.Intent
import android.net.Uri
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
import com.ocak.adminapp.databinding.ActivityMaps4Binding
import java.util.*

class MapsActivity4 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps4Binding
    var latitude =""
    var longitude =""
    var otelAdi=""
    var otelGorseli: Uri?=null
    var otelTelefon=""
    var otelWebAdresi=""
    var otelAciklama=""
    var otelYildizSayisi=""

    var otelWifiBilgisi=""
    var otelRestorantBilgisi=""
    var otelBarBilgisi=""
    var otelAcikBufeKahvaltiBilgisi=""
    var otelSpaMasajBilgisi=""
    var otelSporSalonuBilgisi=""
    var otelSaunaBilgisi=""
    var otelParkAlaniBilgisi=""
    var otelEngelliBilgisi=""
    var otelCocukBilgisi=""
    var otelEvcilHayvanBilgisi=""
    var otelToplantiOdasiBilgisi=""
    var otelCamasirhaneBilgisi=""
    var otelHavuzBilgisi=""


    private  lateinit var  storage: FirebaseStorage
    private lateinit var  database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        database= FirebaseFirestore.getInstance()

       otelAdi=intent.getStringExtra("oteladi").toString()
       otelAciklama=intent.getStringExtra("otelaciklama").toString()
       otelGorseli=intent.getStringExtra("otelGorseli")!!.toUri()
       otelTelefon=intent.getStringExtra("oteltelefon").toString()
       otelWebAdresi=intent.getStringExtra("otelweb").toString()
       otelYildizSayisi=intent.getStringExtra("otelyildiz").toString()
       otelHavuzBilgisi=intent.getStringExtra("otelhavuz").toString()
        otelAcikBufeKahvaltiBilgisi=intent.getStringExtra("otelacikbufekahvalti").toString()
        otelCamasirhaneBilgisi=intent.getStringExtra("otelcamasirhane").toString()
        otelBarBilgisi=intent.getStringExtra("otelbar").toString()
        otelParkAlaniBilgisi=intent.getStringExtra("otelpark").toString()
        otelCocukBilgisi=intent.getStringExtra("otelcocuk").toString()
        otelEngelliBilgisi=intent.getStringExtra("otelengelli").toString()
        otelEvcilHayvanBilgisi=intent.getStringExtra("otelevcilhayvan").toString()
        otelRestorantBilgisi=intent.getStringExtra("otelrestorant").toString()
        otelSaunaBilgisi=intent.getStringExtra("otelsauna").toString()
        otelSpaMasajBilgisi=intent.getStringExtra("otelspamasaj").toString()
        otelSporSalonuBilgisi=intent.getStringExtra("otelsporsalonu").toString()
        otelToplantiOdasiBilgisi=intent.getStringExtra("oteltoplantiodasi").toString()
        otelWifiBilgisi=intent.getStringExtra("otelwifi").toString()



        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
            mMap.addMarker(MarkerOptions().position(p0!!).title(otelAdi))
            latitude = p0.latitude.toString()
            longitude =p0.longitude.toString()

            Toast.makeText(applicationContext,"Artık bu oteli kayıt edebilirsin!", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.oteli_kaydet,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.oteliKaydet){
            otelikaydetFirebase()
        }

        return super.onOptionsItemSelected(item)
    }


    fun otelikaydetFirebase(){

        val uuid = UUID.randomUUID()
        val gorselIsmi = " ${uuid}.jpg"

        val reference = storage.reference
        val gorselReference = reference.child("OtelGorselleri").child(gorselIsmi)


        gorselReference.putFile(otelGorseli!!).addOnSuccessListener {
            val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("OtelGorselleri").child(gorselIsmi)
            yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

                val downloadUri = it.toString()

                val postHashMap = hashMapOf<String,Any>()
                postHashMap.put("otelGorseli",downloadUri)
                postHashMap.put("otelAdi", otelAdi)
                postHashMap.put("latitude",latitude)
                postHashMap.put("longitude",longitude)
                postHashMap.put("otelAciklama",otelAciklama)
                postHashMap.put("otelTelefon",otelTelefon)
                postHashMap.put("otelWeb",otelWebAdresi)
                postHashMap.put("otelYildizSayisi",otelYildizSayisi)
                postHashMap.put("otelhavuz",otelHavuzBilgisi)
                postHashMap.put("otelacikbufekahvalti",otelAcikBufeKahvaltiBilgisi)
                postHashMap.put("otelcamasirhane",otelCamasirhaneBilgisi)
                postHashMap.put("otelbar",otelBarBilgisi)
                postHashMap.put("otelpark",otelParkAlaniBilgisi)
                postHashMap.put("otelcocuk",otelCocukBilgisi)
                postHashMap.put("otelengelli",otelEngelliBilgisi)
                postHashMap.put("otelevcilhayvan",otelEvcilHayvanBilgisi)
                postHashMap.put("otelrestorant",otelRestorantBilgisi)
                postHashMap.put("otelsauna",otelSaunaBilgisi)
                postHashMap.put("otelspamasaj",otelSpaMasajBilgisi)
                postHashMap.put("otelsporsalonu",otelSporSalonuBilgisi)
                postHashMap.put("oteltoplantiodasi",otelToplantiOdasiBilgisi)
                postHashMap.put("otelwifi",otelWifiBilgisi)




                database.collection("Oteller").add(postHashMap).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(applicationContext,"Otel oluşturuldu.", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, OtelListesiActivity::class.java)
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