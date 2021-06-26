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
import kotlinx.android.synthetic.main.activity_istasyon_detay.*
import kotlinx.android.synthetic.main.activity_restorant_detayi.*
import java.util.*

class RestorantDetayiActivity : AppCompatActivity(){


    var secilenRestorant = ""
    var restorantId = ""

    var latitude = ""
    var longitude = ""

    var userLatitude =""
    var userLongitude = ""

    var restorantTelefon =""

    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restorant_detayi)


        database = FirebaseFirestore.getInstance()
        auth= FirebaseAuth.getInstance()


        secilenRestorant = intent.getStringExtra("ad").toString()
        restorantId = intent.getStringExtra("id").toString()


        verileriAl()


        restorantaRotaYap.setOnClickListener {

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



        restorantDetayiTelefonButonu.setOnClickListener {

            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"+restorantTelefon))
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

        val geocoder = Geocoder(this@RestorantDetayiActivity, Locale.getDefault())
        var  adresler : List<Address>

        LocationServices.getFusedLocationProviderClient(this@RestorantDetayiActivity)
            .requestLocationUpdates(locationRequest,object : LocationCallback(){

                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(this@RestorantDetayiActivity)
                        .removeLocationUpdates(this)
                    if (locationResult !=null && locationResult.locations.size > 0){

                        var locIndex = locationResult.locations.size-1

                        userLatitude = locationResult.locations.get(locIndex).latitude.toString()
                        userLongitude = locationResult.locations.get(locIndex).longitude.toString()

                        adresler = geocoder.getFromLocation(userLatitude.toDouble(),userLongitude.toDouble(),1)

                        var adres : String = adresler.get(0).getAddressLine(0)

                        val builder = AlertDialog.Builder(this@RestorantDetayiActivity)


                        builder.setTitle("Rota onayı")


                        builder.setMessage("Konumunuz (${adres}) ile ${secilenRestorant} için  rota oluşturmayı onaylıyor musunuz?")


                        builder.setPositiveButton(
                            "Evet") { dialog, id ->

                            Toast.makeText(this@RestorantDetayiActivity, "Rota başarıyla oluşturuldu",
                                Toast.LENGTH_SHORT).show()
                            val intent = Intent (this@RestorantDetayiActivity,MapsActivity5::class.java)
                            intent.putExtra("latitude",latitude)
                            intent.putExtra("longitude",longitude)
                            intent.putExtra("ad",secilenRestorant)
                            intent.putExtra("userLatitude",userLatitude)
                            intent.putExtra("userLongitude",userLongitude)
                            this@RestorantDetayiActivity.startActivity(intent)

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



        database.collection("Restorantlar").whereEqualTo("restorantAdi",secilenRestorant).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val restorantAdi = document.get("restorantAdi") as String
                            latitude = document.get("latitude") as String
                            longitude = document.get("longitude") as String
                            val restorantGorseli = document.get("restorantGorseli") as String
                            val menuGorseli = document.get("menuGorseli") as String
                            val restorantTipi = document.get("restorantTipi") as String
                            restorantTelefon=document.get("restorantTelefonu") as String
                            val ortalamaTutar = document.get("ortalamaTutar") as String
                            val pazartesi = document.get("pazartesi") as String
                            val sali = document.get("sali") as String
                            val carsamba =document.get("carsamba") as String
                            val persembe = document.get("persembe") as String
                            val cuma = document.get("cuma") as String
                            val cumartesi = document.get("cumartesi") as String
                            val pazar = document.get("pazar") as String

                            val wifiBilgisi = document.get("wifi") as String
                            val sigaraAlaniBilgisi = document.get("sigaraalani") as String
                            val cocukAlaniBilgisi = document.get("cocukalani") as String
                            val yerindeServisBilgisi = document.get("yerindeservis") as String
                            val paketServisBilgisi = document.get("paketservis") as String
                            val disMekanBilgisi = document.get("dismekan") as String
                            val icMekanBilgisi = document.get("icmekan") as String
                            val gelAlBilgisi = document.get("gelal") as String

                            restorantDetayiAd.text = restorantAdi
                            restorantDetayiTipi.text = "Mutfak türü:"+" "+restorantTipi
                            restorantDetayiOrtalamaTutar.text ="2 kişi için ortalama tutar"+" "+ortalamaTutar+" "+"TL."
                            restorantDetayiPazartesi.text="Pazartesi:"+" "+pazartesi
                            restorantDetayiSali.text="Salı:"+" "+sali
                            restorantDetayiCarsamba.text="Çarşamba:"+" "+carsamba
                            restorantDetayiPersembe.text="Perşembe:"+" "+persembe
                            restorantDetayiCuma.text="Cuma:"+" "+cuma
                            restorantDetayiCumartesi.text="Cumartesi:"+" "+cumartesi
                            restorantDetayiPazar.text="Pazar:"+" "+pazar

                            restorantDetayiCocukAlani.text=cocukAlaniBilgisi
                            restorantDetayiDisMekan.text=disMekanBilgisi
                            restorantDetayiGelAl.text=gelAlBilgisi
                            restorantDetayiIcMekan.text=icMekanBilgisi
                            restorantDetayiPaketServis.text=paketServisBilgisi
                            restorantDetayiSigaraAlani.text=sigaraAlaniBilgisi
                            restorantDetayiWifi.text=wifiBilgisi
                            restorantDetayiYerindeServis.text=yerindeServisBilgisi


                            Picasso.get().load(restorantGorseli).into(restorantDetayiResim)
                            Picasso.get().load(menuGorseli).into(restorantDetayiMenu)

                        }


                    }
                }
            }



        }

    }


    fun restorantaYorumYap(view: View){

        val restorantYorumu = restorantYorumYapText.text.toString()
        val restorantPuani = ratingBarRestorant.rating.toString()
        val kullaniciAdi = auth.currentUser!!.displayName
        val restorantYorumTarihi = Timestamp.now()
        val kullaniciUid = auth.currentUser!!.uid

        if (restorantYorumu.isEmpty() == false && restorantPuani.isEmpty() == false ){

            val postHashMap = hashMapOf<String,Any>()

            postHashMap.put("restorantYorumu",restorantYorumu)
            postHashMap.put("yorumuYapanKullanici",kullaniciAdi)
            postHashMap.put("tarih",restorantYorumTarihi)
            postHashMap.put("restorantPuan",restorantPuani)
            postHashMap.put("kullaniciUid",kullaniciUid)

            database.collection("Restorantlar").document(restorantId).collection("RestorantYorumlari").add(postHashMap).addOnCompleteListener {

                if (it.isSuccessful){

                    Toast.makeText(this,"Restorant hakkındaki yorumunuz başarıyla paylaşıldı.",
                        Toast.LENGTH_SHORT).show()


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
        menuInflater.inflate(R.menu.restorant_yorum,menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if(item.itemId == R.id.restorantYorum){
            val intent = Intent(this,RestorantYorumlariActivity::class.java)
            intent.putExtra("restorantId",restorantId)
            startActivity(intent)
        }


        return super.onOptionsItemSelected(item)
    }






}