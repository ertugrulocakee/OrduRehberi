package com.ocak.adminapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.ocak.adminapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hayvan_detayi.*


class HayvanIsiDetayiActivity : AppCompatActivity() {

    var secilenHayvan = ""
    var hayvanId = ""

    private lateinit var database: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hayvan_detayi)

        database = FirebaseFirestore.getInstance()

        val intent = getIntent()
        secilenHayvan = intent.getStringExtra("ad").toString()
        hayvanId= intent.getStringExtra("id").toString()


        verileriAl()


    }

    fun verileriAl() {



        database.collection("Hayvanlar").whereEqualTo("hayvanturu",secilenHayvan).addSnapshotListener { snapshot, exception ->

            if (exception != null){

                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()

            }else{

                if (snapshot != null){

                    if(snapshot.isEmpty == false){


                        val documents = snapshot.documents

                        for( document in documents){


                            val hayvanGorseli = document.get("hayvangorseli") as String
                            val hayvanTuru = document.get("hayvanturu") as String
                            val hayvanBakimi = document.get("hayvanbakimi") as String


                            hayvan_detayi_turu.text="Hayvanın türü:"+" "+hayvanTuru
                            hayvan_detayi_bakim.text=hayvanBakimi
                            Picasso.get().load(hayvanGorseli).into(hayvan_detayi_resim)


                        }


                    }
                }
            }



        }



    }







}