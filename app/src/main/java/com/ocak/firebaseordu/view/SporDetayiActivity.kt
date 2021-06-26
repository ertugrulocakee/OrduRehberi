package com.ocak.firebaseordu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.firebaseordu.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_spor_detayi.*

class SporDetayiActivity : AppCompatActivity() {


    var secilenSpor = ""
    var sporId = ""

    private lateinit var database: FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spor_detayi)

        database = FirebaseFirestore.getInstance()

        val intent = getIntent()
        secilenSpor = intent.getStringExtra("ad").toString()
        sporId= intent.getStringExtra("id").toString()


        verileriAl()


    }

    fun verileriAl() {



        database.collection("Spor").whereEqualTo("sporadi",secilenSpor).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){

                            val sporAdi = document.get("sporadi") as String
                            val sporGorseli = document.get("sporgorseli") as String
                            val sporTuru = document.get("sporturu") as String
                            val sporAlani = document.get("sporAlani") as String
                            val sporAciklama = document.get("sporAciklama") as String

                            spor_detayi_ad.text = sporAdi
                            spor_detayi_turu.text = sporTuru
                            spor_detayi_alan.text = "İlgilenilen spor dalı:"+" "+sporAlani
                            spor_detayi_aciklama.text = sporAciklama


                            Picasso.get().load(sporGorseli).into(spor_detayi_resim)


                        }


                    }
                }
            }



        }



    }





}