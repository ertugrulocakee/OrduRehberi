package com.ocak.adminapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_mekan_detayi.*
import kotlinx.android.synthetic.main.activity_yemek_detayi.*

class YemekDetayiActivity : AppCompatActivity() {

    var secilenYemek = ""
    var yemekId = ""

    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yemek_detayi)

        database = FirebaseFirestore.getInstance()

        val intent = getIntent()
        secilenYemek = intent.getStringExtra("ad").toString()
        yemekId= intent.getStringExtra("id").toString()


        verileriAl()

    }


    fun verileriAl() {



        database.collection("Yemekler").whereEqualTo("yemekadi",secilenYemek).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val yemekAdi = document.get("yemekadi") as String
                            val yemekGorseli = document.get("yemekgorseli") as String
                            val yemekTuru = document.get("yemekturu") as String
                            val yemekTarifi = document.get("yemektarifi") as String
                            val yemekMalzemesi = document.get("yemekmalzemesi") as String

                            yemek_detayi_ad.text = yemekAdi
                            yemek_detayi_turu.text="Yemeğin çeşidi:"+" "+yemekTuru
                            yemek_detayi_malzeme.text = yemekMalzemesi
                            yemek_detayi_tarif.text = yemekTarifi


                            Picasso.get().load(yemekGorseli).into(yemek_detayi_resim)


                        }


                    }
                }
            }



        }



    }







}