package com.ocak.adminapp.view

import android.content.Intent
import android.net.Uri
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
import com.ocak.adminapp.databinding.ActivityMaps2Binding
import java.util.*

class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps2Binding
    var latitude =""
    var longitude =""
    var mekanGorseli=""
    var mekanAdi=""
    var mekanAciklama=""
    var mekanTipi=""


    private  lateinit var  storage: FirebaseStorage
    private lateinit var  database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaps2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        database= FirebaseFirestore.getInstance()

        mekanGorseli= intent.getStringExtra("mekanEkleGorsel").toString()
        mekanAdi=intent.getStringExtra("geziMekaniAdi").toString()
        mekanAciklama=intent.getStringExtra("geziMekaniAciklama").toString()
        mekanTipi=intent.getStringExtra("geziMekaniTipi").toString()


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.gezi_mekani_kaydet,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.gezi_Mekani_Kaydet){
            gezimekanikaydetFirebase()
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
            mMap.addMarker(MarkerOptions().position(p0!!).title(mekanAdi))
            latitude = p0.latitude.toString()
            longitude =p0.longitude.toString()

            Toast.makeText(applicationContext,"Artık bu mekanı kayıt edebilirsin!", Toast.LENGTH_SHORT).show()

        }
    }


   fun gezimekanikaydetFirebase(){

       val uuid = UUID.randomUUID()
       val gorselIsmi = " ${uuid}.jpg"

       val reference = storage.reference
       val gorselReference = reference.child("MekanGorselleri").child(gorselIsmi)
       val mekanGorseliUri =mekanGorseli.toUri()

       gorselReference.putFile(mekanGorseliUri!!).addOnSuccessListener {
           val yuklenenGorselinReference = FirebaseStorage.getInstance().reference.child("MekanGorselleri").child(gorselIsmi)
           yuklenenGorselinReference.downloadUrl.addOnSuccessListener {

               val downloadUri = it.toString()

               val postHashMap = hashMapOf<String,Any>()
               postHashMap.put("mekanGorseli",downloadUri)
               postHashMap.put("mekanAd", mekanAdi)
               postHashMap.put("latitude",latitude)
               postHashMap.put("longitude",longitude)
               postHashMap.put("mekanTipi",mekanTipi)
               postHashMap.put("mekanAciklama",mekanAciklama)


               database.collection("GeziMekanlari").add(postHashMap).addOnCompleteListener {
                   if (it.isSuccessful){
                       Toast.makeText(applicationContext,"Mekan oluşturuldu.", Toast.LENGTH_LONG).show()
                       val intent = Intent(this, GeziKitabiActivity::class.java)
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