package com.ocak.adminapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_istasyon_detay.*
import java.util.*

class IstasyonDetayActivity : AppCompatActivity(){



    var secilenIstasyon = ""
    var istasyonId = ""

    var latitude =""
    var longitude =""

    var userLatitude =""
    var userLongitude =""

    var istasyonWebi =""
    var istasyonTelefonu =""


    private lateinit var database:FirebaseFirestore
    private lateinit var auth: FirebaseAuth




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_istasyon_detay)




        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()



        val intent = getIntent()
        secilenIstasyon = intent.getStringExtra("ad").toString()
        istasyonId = intent.getStringExtra("id").toString()

        verileriAl()


        istasyonaRotaYap.setOnClickListener {
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

        istasyonDetayiWebButonu.setOnClickListener {

            val intent = Intent(this,WebActivity::class.java)
            intent.putExtra("web",istasyonWebi)
            startActivity(intent)


        }


        button_istasyonArama.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"+istasyonTelefonu))
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
            Toast.makeText(this,"İzin alınamadı!",Toast.LENGTH_SHORT).show()

         }
     }


    }


    fun lokasyonAl(){

        var locationRequest = LocationRequest()

        locationRequest.interval= 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            return
        }

        val geocoder = Geocoder(this@IstasyonDetayActivity, Locale.getDefault())
        var  adresler : List<Address>

        LocationServices.getFusedLocationProviderClient(this@IstasyonDetayActivity)
            .requestLocationUpdates(locationRequest,object :LocationCallback(){

                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@IstasyonDetayActivity)
                        .removeLocationUpdates(this)
                    if (locationResult !=null && locationResult.locations.size > 0){

                        var locIndex = locationResult.locations.size-1

                        userLatitude = locationResult.locations.get(locIndex).latitude.toString()
                        userLongitude = locationResult.locations.get(locIndex).longitude.toString()

                        adresler = geocoder.getFromLocation(userLatitude.toDouble(),userLongitude.toDouble(),1)

                        var adres : String = adresler.get(0).getAddressLine(0)

                        val builder = AlertDialog.Builder(this@IstasyonDetayActivity)


                        builder.setTitle("Rota onayı")


                        builder.setMessage("Konumunuz (${adres}) ile ${secilenIstasyon} için  rota oluşturmayı onaylıyor musunuz?")


                        builder.setPositiveButton(
                            "Evet") { dialog, id ->

                            Toast.makeText(this@IstasyonDetayActivity, "Rota başarıyla oluşturuldu",Toast.LENGTH_SHORT).show()
                            val intent = Intent (this@IstasyonDetayActivity,MapsActivity5::class.java)
                            intent.putExtra("latitude",latitude)
                            intent.putExtra("longitude",longitude)
                            intent.putExtra("ad",secilenIstasyon)
                            intent.putExtra("userLatitude",userLatitude)
                            intent.putExtra("userLongitude",userLongitude)
                            this@IstasyonDetayActivity.startActivity(intent)

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




        database.collection("Stations").whereEqualTo("stationad",secilenIstasyon).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val istasyonAd = document.get("stationad") as String
                            val istasyonKapasite = document.get("stationkapasite") as String
                            val faaliyetTarihi = document.get("faaliyettarihi") as String
                            val istasyonResmi = document.get("gorselstation") as String
                            latitude = document.get("latitude") as String
                            longitude = document.get("longitude") as String
                            val istasyonTipi = document.get("istasyonTipi") as String
                            istasyonTelefonu = document.get("istasyonTelefonu") as String
                            istasyonWebi=document.get("istasyonWeb") as String

                            istasyon_ad.text = istasyonAd
                            istasyon_kapasite.text = "İstasyonun yolcu kapasitesi:"+" "+istasyonKapasite
                            istasyon_faaliyet.text = "İstasyonun faaliyete giriş tarihi:"+" "+faaliyetTarihi
                            istasyon_tipi.text = "İstasyonun tipi:"+" "+istasyonTipi


                            Picasso.get().load(istasyonResmi).into(image_istasyon)


                        }


                    }
                }
            }



        }



    }


    fun istasyonaYorumYap(view: View){

        val istasyonYorumu = istasyonYorumYapText.text.toString()
        val istasyonPuani = ratingBarIstasyon.rating.toString()
        val kullaniciAdi = auth.currentUser!!.displayName
        val kullaniciUid = auth.currentUser!!.uid
        val istasyonYorumTarihi = Timestamp.now()

        if (istasyonYorumu.isEmpty() == false && istasyonPuani.isEmpty() == false ){

            val postHashMap = hashMapOf<String,Any>()

            postHashMap.put("istasyonYorumu",istasyonYorumu)
            postHashMap.put("yorumuYapanKullanici",kullaniciAdi)
            postHashMap.put("tarih",istasyonYorumTarihi)
            postHashMap.put("istasyonPuan",istasyonPuani)
            postHashMap.put("kullaniciUid",kullaniciUid)

            database.collection("Stations").document(istasyonId).collection("IstasyonYorumlari").add(postHashMap).addOnCompleteListener {

                if (it.isSuccessful){

                    Toast.makeText(this,"İstasyon hakkındaki yorumunuz başarıyla paylaşıldı.",Toast.LENGTH_SHORT).show()


                }
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT).show()
            }


        }else{
            Toast.makeText(this,"Eksik işlem var!",Toast.LENGTH_LONG).show()
        }



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.istasyon_yorum,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if(item.itemId == R.id.istasyonYorum){
            val intent = Intent(this,IstasyonYorumlariActivity::class.java)
            intent.putExtra("istasyonId",istasyonId)
            startActivity(intent)
        }


        return super.onOptionsItemSelected(item)
    }





}