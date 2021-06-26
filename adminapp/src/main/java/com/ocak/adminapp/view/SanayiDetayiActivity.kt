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
import kotlinx.android.synthetic.main.activity_istasyon_detay.*
import kotlinx.android.synthetic.main.activity_istasyon_detay.istasyonaRotaYap
import kotlinx.android.synthetic.main.activity_sanayi_detayi.*
import java.util.*

class SanayiDetayiActivity : AppCompatActivity() {


    var secilenFirma = ""
    var firmaId = ""

    var latitude =""
    var longitude =""

    var userLatitude =""
    var userLongitude =""

    var firmaWebi =""
    var firmaTelefonu =""


    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sanayi_detayi)


        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()



        val intent = getIntent()
        secilenFirma = intent.getStringExtra("ad").toString()
        firmaId = intent.getStringExtra("id").toString()

        verileriAl()


        sanayiyeRotaYap.setOnClickListener {
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

        sanayidetayi_Web.setOnClickListener {

            val intent = Intent(this,WebActivity::class.java)
            intent.putExtra("web",firmaWebi)
            startActivity(intent)


        }


        sanayidetayi_Telefon.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"+firmaTelefonu))
            startActivity(intent)


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

        val geocoder = Geocoder(this@SanayiDetayiActivity, Locale.getDefault())
        var  adresler : List<Address>

        LocationServices.getFusedLocationProviderClient(this@SanayiDetayiActivity)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){

                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@SanayiDetayiActivity)
                        .removeLocationUpdates(this)
                    if (locationResult !=null && locationResult.locations.size > 0){

                        var locIndex = locationResult.locations.size-1

                        userLatitude = locationResult.locations.get(locIndex).latitude.toString()
                        userLongitude = locationResult.locations.get(locIndex).longitude.toString()

                        adresler = geocoder.getFromLocation(userLatitude.toDouble(),userLongitude.toDouble(),1)

                        var adres : String = adresler.get(0).getAddressLine(0)

                        val builder = AlertDialog.Builder(this@SanayiDetayiActivity)


                        builder.setTitle("Rota onayı")


                        builder.setMessage("Konumunuz (${adres}) ile ${secilenFirma} için  rota oluşturmayı onaylıyor musunuz?")


                        builder.setPositiveButton(
                            "Evet") { dialog, id ->

                            Toast.makeText(this@SanayiDetayiActivity, "Rota başarıyla oluşturuldu",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent (this@SanayiDetayiActivity,MapsActivity5::class.java)
                            intent.putExtra("latitude",latitude)
                            intent.putExtra("longitude",longitude)
                            intent.putExtra("ad",secilenFirma)
                            intent.putExtra("userLatitude",userLatitude)
                            intent.putExtra("userLongitude",userLongitude)
                            this@SanayiDetayiActivity.startActivity(intent)

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




        database.collection("SanayiFirmalari").whereEqualTo("sanayiad",secilenFirma).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val firmaAd = document.get("sanayiad") as String
                            val firmaAlani = document.get("sanayialani") as String
                            val firmaAciklama = document.get("sanayiaciklama") as String
                            val firmaGorseli = document.get("sanayigorsel") as String
                            latitude = document.get("latitude") as String
                            longitude = document.get("longitude") as String
                            firmaTelefonu = document.get("sanayitelefon") as String
                            firmaWebi=document.get("sanayiWeb") as String

                            sanayidetayi_ad.text = firmaAd
                            sanayidetayi_alani.text = "Firmanın icraat alanı:"+" "+firmaAlani
                            sanayidetayi_aciklama.text=firmaAciklama

                            Picasso.get().load(firmaGorseli).into(sanayi_detayi_resim)


                        }


                    }
                }
            }



        }



    }







}