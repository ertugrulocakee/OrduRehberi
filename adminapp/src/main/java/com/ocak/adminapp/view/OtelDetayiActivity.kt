package com.ocak.adminapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_otel_detayi.*
import kotlinx.android.synthetic.main.activity_share_post.*

import java.util.*


class OtelDetayiActivity : AppCompatActivity()  {

    var secilenOtel=""
    var otelId = ""


    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    var latitude =""
    var longitude =""

    var userLatitude =""
    var userLongitude =""

    var otelTelefon =""
    var otelWeb = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otel_detayi)


        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        val intent = getIntent()
        secilenOtel = intent.getStringExtra("ad").toString()
        otelId = intent.getStringExtra("id").toString()

        verileriAl()


        oteleRotaYap.setOnClickListener {

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


        otelDetayiTelefonButonu.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"+otelTelefon))
            startActivity(intent)


        }


        otelDetayiWebButonu.setOnClickListener {

            val intent = Intent(this,WebActivity::class.java)
            intent.putExtra("web",otelWeb)
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
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            return
        }

        val geocoder = Geocoder(this@OtelDetayiActivity, Locale.getDefault())
        var  adresler : List<Address>

        LocationServices.getFusedLocationProviderClient(this@OtelDetayiActivity)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){

                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@OtelDetayiActivity)
                        .removeLocationUpdates(this)
                    if (locationResult !=null && locationResult.locations.size > 0){

                        var locIndex = locationResult.locations.size-1

                        userLatitude = locationResult.locations.get(locIndex).latitude.toString()
                        userLongitude = locationResult.locations.get(locIndex).longitude.toString()

                        adresler = geocoder.getFromLocation(userLatitude.toDouble(),userLongitude.toDouble(),1)

                        var adres : String = adresler.get(0).getAddressLine(0)

                        val builder = AlertDialog.Builder(this@OtelDetayiActivity)


                        builder.setTitle("Rota onayı")


                        builder.setMessage("Konumunuz (${adres}) ile ${secilenOtel} için  rota oluşturmayı onaylıyor musunuz?")


                        builder.setPositiveButton(
                            "Evet") { dialog, id ->

                            Toast.makeText(this@OtelDetayiActivity, "Rota başarıyla oluşturuldu",Toast.LENGTH_SHORT).show()
                            val intent = Intent (this@OtelDetayiActivity,MapsActivity5::class.java)
                            intent.putExtra("latitude",latitude)
                            intent.putExtra("longitude",longitude)
                            intent.putExtra("ad",secilenOtel)
                            intent.putExtra("userLatitude",userLatitude)
                            intent.putExtra("userLongitude",userLongitude)
                            this@OtelDetayiActivity.startActivity(intent)

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

        database.collection("Oteller").whereEqualTo("otelAdi",secilenOtel).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val otelAdi = document.get("otelAdi") as String
                            latitude = document.get("latitude") as String
                            longitude = document.get("longitude") as String
                            val otelGorseli = document.get("otelGorseli") as String
                             otelTelefon=document.get("otelTelefon") as String
                            val otelAciklama = document.get("otelAciklama") as String
                             otelWeb = document.get("otelWeb") as String
                            val otelYildizSayisi =document.get("otelYildizSayisi") as String
                            val otelHavuzBilgisi=document.get("otelhavuz") as String
                            val otelAcikBufeKahvaltiBilgisi= document.get("otelacikbufekahvalti") as String
                            val otelCamasirhaneBilgisi = document.get("otelcamasirhane") as String
                            val otelBarBilgisi = document.get("otelbar") as String
                            val otelParkBilgisi = document.get("otelpark") as String
                            val otelCocukBilgisi = document.get("otelcocuk") as String
                            val otelEngelliBilgisi = document.get("otelcocuk") as String
                            val otelEvcilHayvanBilgisi = document.get("otelevcilhayvan") as String
                            val otelRestorantBilgisi = document.get("otelrestorant") as String
                            val otelSaunaBilgisi = document.get("otelsauna") as String
                            val otelSpaMasajBilgisi = document.get("otelspamasaj") as String
                            val otelSporSalonuBilgisi = document.get("otelsporsalonu") as String
                            val otelToplantiOdasiBilgisi = document.get("oteltoplantiodasi") as String
                            val otelWifiBilgisi = document.get("otelwifi") as String


                            otelDetayiAd.text=otelAdi
                            otelDetayiAciklama.text=otelAciklama
                            otelDetayiYildiz.text="Otel"+" "+otelYildizSayisi+" "+"yıldızlı bir oteldir."
                            otelDetayiHavuz.text=otelHavuzBilgisi
                            otelDetayiAcikBufeKahvalti.text=otelAcikBufeKahvaltiBilgisi
                            otelDetayiBar.text=otelBarBilgisi
                            otelDetayiCamasirhane.text=otelCamasirhaneBilgisi
                            otelDetayiCocukBilgisi.text=otelCocukBilgisi
                            otelDetayiEngelliUygunluk.text=otelEngelliBilgisi
                            otelDetayiEvcilHayvanUygunluk.text=otelEvcilHayvanBilgisi
                            otelDetayiParkAlani.text=otelParkBilgisi
                            otelDetayiRestorant.text=otelRestorantBilgisi
                            otelDetayiSauna.text=otelSaunaBilgisi
                            otelDetayiSpaMasaj.text=otelSpaMasajBilgisi
                            otelDetayiSporSalonu.text=otelSporSalonuBilgisi
                            otelDetayiToplantiOdasi.text=otelToplantiOdasiBilgisi
                            otelDetayiWifi.text=otelWifiBilgisi

                            Picasso.get().load(otelGorseli).into(otelDetayiGorseli)

                        }


                    }
                }
            }



        }

    }


    fun oteleYorumYap(view:View){

        val otelYorumu = oteleYorumYapText.text.toString()
        val otelPuani = ratingBarOtel.rating.toString()
        val kullaniciAdi = auth.currentUser!!.displayName
        val kullaniciUid = auth.currentUser!!.uid
        val otelYorumTarihi = Timestamp.now()

        if (otelYorumu.isEmpty() == false && otelPuani.isEmpty() == false ){

            val postHashMap = hashMapOf<String,Any>()

            postHashMap.put("otelYorumu",otelYorumu)
            postHashMap.put("yorumuYapanKullanici",kullaniciAdi)
            postHashMap.put("tarih",otelYorumTarihi)
            postHashMap.put("otelPuan",otelPuani)
            postHashMap.put("kullaniciUid",kullaniciUid)

            database.collection("Oteller").document(otelId).collection("OtelYorumlari").add(postHashMap).addOnCompleteListener {

                if (it.isSuccessful){

                    Toast.makeText(this,"Otel hakkındaki yorumunuz başarıyla paylaşıldı.",Toast.LENGTH_SHORT).show()


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
        menuInflater.inflate(R.menu.otel_yorum,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if(item.itemId == R.id.otelYorum){
            val intent = Intent(this,OtelYorumlariActivity::class.java)
            intent.putExtra("otelId",otelId)
            startActivity(intent)
        }


        return super.onOptionsItemSelected(item)
    }



}




