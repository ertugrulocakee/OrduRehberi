package com.ocak.firebaseordu.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mekan_detayi.*
import java.util.*

class MekanDetayiActivity : AppCompatActivity()  {

    var secilenMekan = ""
    var mekanId = ""

    var latitude =""
    var longitude =""

    var userLatitude =""
    var userLongitude =""

    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mekan_detayi)

        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()



        val intent = getIntent()
        secilenMekan = intent.getStringExtra("ad").toString()
        mekanId= intent.getStringExtra("id").toString()


        verileriAl()


        mekanaRotaYap.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {

                lokasyonAl()

            }
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1  &&  grantResults.size > 0){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                lokasyonAl()

            }else{
                Toast.makeText(this,"İzin alınamadı!", Toast.LENGTH_SHORT).show()

            }
        }


    }


    fun lokasyonAl(){

        var locationRequest = LocationRequest()

        locationRequest.interval= 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            return
        }

        val geocoder = Geocoder(this@MekanDetayiActivity, Locale.getDefault())
        var  adresler : List<Address>

        LocationServices.getFusedLocationProviderClient(this@MekanDetayiActivity)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){

                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@MekanDetayiActivity)
                        .removeLocationUpdates(this)
                    if (locationResult !=null && locationResult.locations.size > 0){

                        var locIndex = locationResult.locations.size-1

                        userLatitude = locationResult.locations.get(locIndex).latitude.toString()
                        userLongitude = locationResult.locations.get(locIndex).longitude.toString()

                        adresler = geocoder.getFromLocation(userLatitude.toDouble(),userLongitude.toDouble(),1)

                        var adres : String = adresler.get(0).getAddressLine(0)

                        val builder = AlertDialog.Builder(this@MekanDetayiActivity)


                        builder.setTitle("Rota onayı")


                        builder.setMessage("Konumunuz (${adres}) ile ${secilenMekan} için  rota oluşturmayı onaylıyor musunuz?")


                        builder.setPositiveButton(
                            "Evet") { dialog, id ->

                            Toast.makeText(this@MekanDetayiActivity, "Rota başarıyla oluşturuldu",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent (this@MekanDetayiActivity,MapsActivity5::class.java)
                            intent.putExtra("latitude",latitude)
                            intent.putExtra("longitude",longitude)
                            intent.putExtra("ad",secilenMekan)
                            intent.putExtra("userLatitude",userLatitude)
                            intent.putExtra("userLongitude",userLongitude)
                            this@MekanDetayiActivity.startActivity(intent)

                        }


                        builder.setNegativeButton(
                            "Hayır") { dialog, id ->

                        }


                        builder.show()


                    }
                }




            }, Looper.getMainLooper())

    }


    fun verileriAl() {



        database.collection("GeziMekanlari").whereEqualTo("mekanAd",secilenMekan).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val mekanAdi = document.get("mekanAd") as String
                            latitude = document.get("latitude") as String
                            longitude = document.get("longitude") as String
                            val mekanGorseli = document.get("mekanGorseli") as String
                            val mekanAciklama = document.get("mekanAciklama") as String
                            val mekanTipi = document.get("mekanTipi") as String

                            mekan_detayi_ad.text = mekanAdi
                            mekan_detayi_tip.text="Mekanın niteliği:"+" "+mekanTipi
                            mekan_detayi_aciklama.text = mekanAciklama


                            Picasso.get().load(mekanGorseli).into(mekan_detayi_resim)


                        }


                    }
                }
            }



        }



    }


    fun mekanaYorumYap(view: View){

        val mekanYorumu = mekanYorumYapText.text.toString()
        val mekanPuani = ratingBarMekan.rating.toString()
        val kullaniciAdi = auth.currentUser!!.displayName
        val kullaniciUid = auth.currentUser!!.uid
        val mekanYorumTarihi = Timestamp.now()

        if (mekanYorumu.isEmpty() == false && mekanPuani.isEmpty() == false ){

            val postHashMap = hashMapOf<String,Any>()

            postHashMap.put("mekanYorumu",mekanYorumu)
            postHashMap.put("yorumuYapanKullanici",kullaniciAdi)
            postHashMap.put("tarih",mekanYorumTarihi)
            postHashMap.put("mekanPuan",mekanPuani)
            postHashMap.put("kullaniciUid",kullaniciUid)

            database.collection("GeziMekanlari").document(mekanId).collection("MekanYorumlari").add(postHashMap).addOnCompleteListener {

                if (it.isSuccessful){

                    Toast.makeText(this,"Mekan hakkındaki yorumunuz başarıyla paylaşıldı.", Toast.LENGTH_SHORT).show()


                }
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage, Toast.LENGTH_SHORT).show()
            }


        }else{
            Toast.makeText(this,"Eksik işlem var!", Toast.LENGTH_LONG).show()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.mekan_yorum,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if(item.itemId == R.id.mekanYorum){
            val intent = Intent(this,MekanYorumlariActivity::class.java)
            intent.putExtra("mekanId",mekanId)
            startActivity(intent)
        }


        return super.onOptionsItemSelected(item)
    }



}