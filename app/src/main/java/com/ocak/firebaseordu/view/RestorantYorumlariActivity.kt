package com.ocak.firebaseordu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.adapter.RestorantYorumlariAdapter
import com.ocak.firebaseordu.model.RestorantYorumlari
import kotlinx.android.synthetic.main.activity_restorant_yorumlari.*

class RestorantYorumlariActivity : AppCompatActivity() {

    var restorantYorumlariListesi = ArrayList<RestorantYorumlari>()
    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: RestorantYorumlariAdapter
    var restorantDokumanId= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restorant_yorumlari)

        database= FirebaseFirestore.getInstance()

        restorantDokumanId = intent.getStringExtra("restorantId").toString()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewRestorantYorumlari.layoutManager = layoutManager
        recyclerAdapter = RestorantYorumlariAdapter(restorantYorumlariListesi)
        recyclerViewRestorantYorumlari.adapter = recyclerAdapter



    }

    fun verileriAl(){

        database.collection("Restorantlar").document(restorantDokumanId).collection("RestorantYorumlari").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        restorantYorumlariListesi.clear()

                        for ( document in documents){


                            val kullaniciAdi = document.get("yorumuYapanKullanici") as String
                            val kullaniciYorum = document.get("restorantYorumu") as String
                            val restorantPuani = document.get("restorantPuan") as String
                            val restorantYorumId =document.id
                            val kullanici = document.get("kullaniciUid") as String

                            val puan ="Puanım:"+" "+restorantPuani

                            val indirilen = RestorantYorumlari(kullaniciAdi,kullaniciYorum,puan,restorantDokumanId,restorantYorumId,kullanici)

                            restorantYorumlariListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }


        }

    }
}