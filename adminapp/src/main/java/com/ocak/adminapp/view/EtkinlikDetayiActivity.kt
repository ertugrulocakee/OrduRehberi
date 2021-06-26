package com.ocak.adminapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_etkinlik_detayi.*
import kotlinx.android.synthetic.main.activity_sanayi_detayi.*
import kotlinx.android.synthetic.main.activity_sanayi_detayi.sanayiyeRotaYap
import java.util.*

class EtkinlikDetayiActivity : AppCompatActivity() {


    var secilenEtkinlik = ""
    var etkinlikId = ""

    var latitude =""
    var longitude =""

    var userLatitude =""
    var userLongitude =""



    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_etkinlik_detayi)


        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()



        val intent = getIntent()
        secilenEtkinlik = intent.getStringExtra("ad").toString()
        etkinlikId = intent.getStringExtra("id").toString()

        verileriAl()


        etkinligeRotaYap.setOnClickListener {
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

        val geocoder = Geocoder(this@EtkinlikDetayiActivity, Locale.getDefault())
        var  adresler : List<Address>

        LocationServices.getFusedLocationProviderClient(this@EtkinlikDetayiActivity)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){

                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@EtkinlikDetayiActivity)
                        .removeLocationUpdates(this)
                    if (locationResult !=null && locationResult.locations.size > 0){

                        var locIndex = locationResult.locations.size-1

                        userLatitude = locationResult.locations.get(locIndex).latitude.toString()
                        userLongitude = locationResult.locations.get(locIndex).longitude.toString()

                        adresler = geocoder.getFromLocation(userLatitude.toDouble(),userLongitude.toDouble(),1)

                        var adres : String = adresler.get(0).getAddressLine(0)

                        val builder = AlertDialog.Builder(this@EtkinlikDetayiActivity)


                        builder.setTitle("Rota onayı")


                        builder.setMessage("Konumunuz (${adres}) ile ${secilenEtkinlik} için  rota oluşturmayı onaylıyor musunuz?")


                        builder.setPositiveButton(
                            "Evet") { dialog, id ->

                            Toast.makeText(this@EtkinlikDetayiActivity, "Rota başarıyla oluşturuldu",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent (this@EtkinlikDetayiActivity,MapsActivity5::class.java)
                            intent.putExtra("latitude",latitude)
                            intent.putExtra("longitude",longitude)
                            intent.putExtra("ad",secilenEtkinlik)
                            intent.putExtra("userLatitude",userLatitude)
                            intent.putExtra("userLongitude",userLongitude)
                            this@EtkinlikDetayiActivity.startActivity(intent)

                        }


                        builder.setNegativeButton(
                            "Hayır") { dialog, id ->

                        }


                        builder.show()


                    }
                }




            }, Looper.getMainLooper())

    }




    fun verileriAl(){




        database.collection("KulturSanatEtkinlikleri").whereEqualTo("etkinlikad",secilenEtkinlik).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val etkinlikAd = document.get("etkinlikad") as String
                            val  etkinlikAlani = document.get("etkinlikalani") as String
                            val etkinlikAciklama = document.get("etkinlikaciklama") as String
                            val etkinlikGorseli = document.get("etkinlikgorsel") as String
                            latitude = document.get("latitude") as String
                            longitude = document.get("longitude") as String
                            val etkinlikBaslangic = document.get("etkinlikbaslangic") as String
                            val etkinlikBitis = document.get("etkinlikbitis") as String

                            etkinlikdetayi_ad.text = etkinlikAd
                            etkinlikdetayi_alani.text = "Etkinliğin alanı:"+" "+etkinlikAlani
                            etkinlikdetayi_aciklama.text= etkinlikAciklama
                            etkinlikdetayi_baslangic.text = "Etkinliğin başlangıç tarihi:"+" "+etkinlikBaslangic
                            etkinlikdetayi_bitis.text = "Etkinliğin bitiş tarihi:"+" "+etkinlikBitis

                            Picasso.get().load(etkinlikGorseli).into(etkinlik_detayi_resim)


                        }


                    }
                }
            }



        }



    }


}