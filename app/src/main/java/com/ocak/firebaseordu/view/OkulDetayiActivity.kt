package com.ocak.firebaseordu.view

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
import com.ocak.firebaseordu.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_okul_detayi.*
import java.util.*

class OkulDetayiActivity : AppCompatActivity() {


    var secilenOkul = ""
    var okulId = ""

    var latitude =""
    var longitude =""

    var userLatitude =""
    var userLongitude =""

    var okulWebi =""
    var okulTelefonu =""


    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_okul_detayi)


        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()



        val intent = getIntent()
        secilenOkul = intent.getStringExtra("ad").toString()
        okulId = intent.getStringExtra("id").toString()

        verileriAl()


        okulaRotaYap.setOnClickListener {
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

        okuldetayi_Web.setOnClickListener {

            val intent = Intent(this,WebActivity::class.java)
            intent.putExtra("web",okulWebi)
            startActivity(intent)


        }


        okuldetayi_Telefon.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"+okulTelefonu))
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

        val geocoder = Geocoder(this@OkulDetayiActivity, Locale.getDefault())
        var  adresler : List<Address>

        LocationServices.getFusedLocationProviderClient(this@OkulDetayiActivity)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){

                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@OkulDetayiActivity)
                        .removeLocationUpdates(this)
                    if (locationResult !=null && locationResult.locations.size > 0){

                        var locIndex = locationResult.locations.size-1

                        userLatitude = locationResult.locations.get(locIndex).latitude.toString()
                        userLongitude = locationResult.locations.get(locIndex).longitude.toString()

                        adresler = geocoder.getFromLocation(userLatitude.toDouble(),userLongitude.toDouble(),1)

                        var adres : String = adresler.get(0).getAddressLine(0)

                        val builder = AlertDialog.Builder(this@OkulDetayiActivity)


                        builder.setTitle("Rota onayı")


                        builder.setMessage("Konumunuz (${adres}) ile ${secilenOkul} için  rota oluşturmayı onaylıyor musunuz?")


                        builder.setPositiveButton(
                            "Evet") { dialog, id ->

                            Toast.makeText(this@OkulDetayiActivity, "Rota başarıyla oluşturuldu",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent (this@OkulDetayiActivity,MapsActivity5::class.java)
                            intent.putExtra("latitude",latitude)
                            intent.putExtra("longitude",longitude)
                            intent.putExtra("ad",secilenOkul)
                            intent.putExtra("userLatitude",userLatitude)
                            intent.putExtra("userLongitude",userLongitude)
                            this@OkulDetayiActivity.startActivity(intent)

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




        database.collection("Okullar").whereEqualTo("okulad",secilenOkul).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val okulAd = document.get("okulad") as String
                            val okulSeviyesi = document.get("okulseviyesi") as String
                            val okulAciklama = document.get("okulaciklama") as String
                            val okulTuru = document.get("okulturu") as String
                            val okulGorseli = document.get("okulgorsel") as String
                            latitude = document.get("latitude") as String
                            longitude = document.get("longitude") as String
                            okulTelefonu = document.get("okultelefon") as String
                            okulWebi=document.get("okulweb") as String

                            okuldetayi_ad.text = okulAd
                            okuldetayi_seviye.text = "Okulun eğitim seviyesi:"+" "+okulSeviyesi
                            okuldetayi_tur.text ="Okulun tipi:"+" "+okulTuru
                            okuldetayi_aciklama.text=okulAciklama

                            Picasso.get().load(okulGorseli).into(okul_detayi_resim)


                        }


                    }
                }
            }



        }



    }


}
