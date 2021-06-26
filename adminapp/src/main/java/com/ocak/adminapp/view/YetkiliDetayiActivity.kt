package com.ocak.adminapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_yetkili_detayi.*

class YetkiliDetayiActivity : AppCompatActivity() {

    var secilenYetkili = ""
    var yetkiliId = ""

    private lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yetkili_detayi)


        database = FirebaseFirestore.getInstance()

        val intent = getIntent()
        secilenYetkili = intent.getStringExtra("ad").toString()
        yetkiliId= intent.getStringExtra("id").toString()


        verileriAl()


    }


    fun verileriAl() {

        database.collection("Yetkililer").whereEqualTo("yetkiliadi",secilenYetkili).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val yetkiliAdi = document.get("yetkiliadi") as String
                            val yetkiliGorseli = document.get("yetkiligorseli") as String
                            val yetkiliUniversite = document.get("yetkiliuniversite") as String
                            val yetkiliBolum = document.get("yetkilibolum") as String
                            val yetkiliAciklama = document.get("yetkiliaciklama") as String
                            val yetkiliMakam = document.get("yetkilimakam") as String
                            val yetkiliMemleket = document.get("yetkilimemleket") as String
                            val yetkiliDogumTarihi = document.get("yetkilidogumtarihi") as String
                            val yetkiliGoreveBaslangic = document.get("yetkiligorevbaslangic") as String

                            yetkili_detayi_ad.text = yetkiliAdi
                            yetkili_detayi_universite.text = yetkiliUniversite
                            yetkili_detayi_bolum.text=yetkiliBolum
                            yetkili_detayi_aciklama.text = yetkiliAciklama
                            yetkili_detayi_makam.text=yetkiliMakam
                            yetkili_detayi_memleket.text="Memleketi:"+" "+yetkiliMemleket
                            yetkili_detayi_dogum_yili.text ="Doğum tarihi:"+" "+yetkiliDogumTarihi
                            yetkili_detayi_gorev_baslangic.text="Göreve başlangıç:"+" "+yetkiliGoreveBaslangic


                            Picasso.get().load(yetkiliGorseli).into(yetkili_detayi_resim)


                        }


                    }
                }
            }



        }



    }


}