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
import com.ocak.adminapp.databinding.ActivityMaps3Binding
import java.util.*

class MapsActivity3 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMaps3Binding

    var latitude =""
    var longitude =""
    var restorantAdi =""
    var restorantTipi =""
    var restorantResmi=""
    var menuResmi =""
    var restorantTelefonu =""
    var ortalamaTutar =""
    var pazartesiSaati=""
    var saliSaati=""
    var carsambaSaati=""
    var persembeSaati=""
    var cumaSaati=""
    var cumartesiSaati=""
    var pazarSaati=""

    var restorantWifiBilgisi=""
    var restorantIcMekanBilgisi=""
    var restorantDisMekanBilgisi=""
    var restorantSigaraAlaniBilgisi=""
    var restorantCocukAlaniBilgisi=""
    var restorantYerindeServisBilgisi=""
    var restorantPaketServisBilgisi=""
    var restorantGelAlBilgisi=""



    private  lateinit var  storage: FirebaseStorage
    private lateinit var  database : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMaps3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()
        database= FirebaseFirestore.getInstance()

        restorantAdi=intent.getStringExtra("restoranAdi").toString()
        restorantTipi = intent.getStringExtra("restoranTipi").toString()
        restorantResmi =intent.getStringExtra("restoranResmi").toString()
        menuResmi=intent.getStringExtra("menuResmi").toString()
        restorantTelefonu=intent.getStringExtra("restorantTelefonu").toString()
        ortalamaTutar=intent.getStringExtra("ortalamaTutar").toString()
        pazartesiSaati=intent.getStringExtra("pazartesi").toString()
        saliSaati=intent.getStringExtra("sali").toString()
        carsambaSaati=intent.getStringExtra("carsamba").toString()
        persembeSaati=intent.getStringExtra("persembe").toString()
        cumaSaati=intent.getStringExtra("cuma").toString()
        cumartesiSaati=intent.getStringExtra("cumartesi").toString()
        pazarSaati=intent.getStringExtra("pazar").toString()
        restorantWifiBilgisi=intent.getStringExtra("wifi").toString()
        restorantCocukAlaniBilgisi=intent.getStringExtra("cocukalani").toString()
        restorantDisMekanBilgisi=intent.getStringExtra("dismekan").toString()
        restorantGelAlBilgisi=intent.getStringExtra("gelal").toString()
        restorantIcMekanBilgisi=intent.getStringExtra("icmekan").toString()
        restorantPaketServisBilgisi=intent.getStringExtra("paketservis").toString()
        restorantSigaraAlaniBilgisi=intent.getStringExtra("sigaraalani").toString()
        restorantYerindeServisBilgisi=intent.getStringExtra("yerindeservis").toString()


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
            mMap.addMarker(MarkerOptions().position(p0!!).title(restorantAdi))
            latitude = p0.latitude.toString()
            longitude =p0.longitude.toString()

            Toast.makeText(applicationContext,"Artık bu restorantı kayıt edebilirsin!", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.restorant_kaydet,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.restorantKaydet){
            restorantikaydetFirebase()
        }

        return super.onOptionsItemSelected(item)
    }



    fun restorantikaydetFirebase(){


        val uuid = UUID.randomUUID()
        val gorselIsmi = " ${uuid}.jpg"

        val reference = storage.reference
        val gorselReferenceRestorant = reference.child("RestorantGorselleri").child(gorselIsmi)
        val gorselReferenceMenu = reference.child("MenuGorselleri").child(gorselIsmi)
        val restorantGorseliUri =restorantResmi.toUri()
        val menuGorseliUri = menuResmi.toUri()

        gorselReferenceRestorant.putFile(restorantGorseliUri!!).addOnSuccessListener {
          gorselReferenceMenu.putFile(menuGorseliUri!!).addOnSuccessListener {

              val yuklenenRestorantGorselinReference = FirebaseStorage.getInstance().reference.child("RestorantGorselleri").child(gorselIsmi)
              val yuklenenMenuGorselinReference= FirebaseStorage.getInstance().reference.child("MenuGorselleri").child(gorselIsmi)
              yuklenenRestorantGorselinReference.downloadUrl.addOnSuccessListener {

                  val downloadUriRestorant = it.toString()

              yuklenenMenuGorselinReference.downloadUrl.addOnSuccessListener {





                  val downloadUriMenu =it.toString()

                  val postHashMap = hashMapOf<String,Any>()
                  postHashMap.put("restorantGorseli",downloadUriRestorant)
                  postHashMap.put("menuGorseli",downloadUriMenu)
                  postHashMap.put("restorantAdi",restorantAdi)
                  postHashMap.put("latitude",latitude)
                  postHashMap.put("longitude",longitude)
                  postHashMap.put("restorantTipi",restorantTipi)
                  postHashMap.put("restorantTelefonu",restorantTelefonu)
                  postHashMap.put("ortalamaTutar",ortalamaTutar)
                  postHashMap.put("pazartesi",pazartesiSaati)
                  postHashMap.put("sali",saliSaati)
                  postHashMap.put("carsamba",carsambaSaati)
                  postHashMap.put("persembe",persembeSaati)
                  postHashMap.put("cuma",cumaSaati)
                  postHashMap.put("cumartesi",cumartesiSaati)
                  postHashMap.put("pazar",pazarSaati)
                  postHashMap.put("wifi",restorantWifiBilgisi)
                  postHashMap.put("sigaraalani",restorantSigaraAlaniBilgisi)
                  postHashMap.put("cocukalani",restorantCocukAlaniBilgisi)
                  postHashMap.put("yerindeservis",restorantYerindeServisBilgisi)
                  postHashMap.put("paketservis",restorantPaketServisBilgisi)
                  postHashMap.put("dismekan",restorantDisMekanBilgisi)
                  postHashMap.put("icmekan",restorantIcMekanBilgisi)
                  postHashMap.put("gelal",restorantGelAlBilgisi)



                  database.collection("Restorantlar").add(postHashMap).addOnCompleteListener {
                      if (it.isSuccessful){
                          Toast.makeText(applicationContext,"Restorant oluşturuldu.", Toast.LENGTH_LONG).show()
                          val intent = Intent(this, RestorantListesiActivity::class.java)
                          startActivity(intent)
                          finish()
                      }
                  }.addOnFailureListener {
                      Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
                  }

              }.addOnFailureListener{
                  Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
              }

              }.addOnFailureListener {
                  Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
              }



          }.addOnFailureListener{
              Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
          }



        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }


}