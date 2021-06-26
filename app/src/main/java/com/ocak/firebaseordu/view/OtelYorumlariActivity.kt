package com.ocak.firebaseordu.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ocak.firebaseordu.R
import com.ocak.firebaseordu.adapter.OtelYorumlariAdapter
import com.ocak.firebaseordu.model.OtelYorumlari
import kotlinx.android.synthetic.main.activity_otel_yorumlari.*

class OtelYorumlariActivity : AppCompatActivity() {

    var otelYorumlariListesi = ArrayList<OtelYorumlari>()
    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: OtelYorumlariAdapter
    var otelDokumanId= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otel_yorumlari)

        database= FirebaseFirestore.getInstance()

        otelDokumanId = intent.getStringExtra("otelId").toString()

        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewOtelYorumlari.layoutManager = layoutManager
        recyclerAdapter = OtelYorumlariAdapter(otelYorumlariListesi)
        recyclerViewOtelYorumlari.adapter = recyclerAdapter



    }


    fun verileriAl(){

        database.collection("Oteller").document(otelDokumanId).collection("OtelYorumlari").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        otelYorumlariListesi.clear()

                        for ( document in documents){


                            val kullaniciAdi = document.get("yorumuYapanKullanici") as String
                            val kullaniciYorum = document.get("otelYorumu") as String
                            val otelPuani = document.get("otelPuan") as String
                            val otelYorumId =document.id
                            val kullanici = document.get("kullaniciUid") as String

                            val puan ="Puanım:"+" "+otelPuani

                            val indirilen = OtelYorumlari(kullaniciAdi,kullaniciYorum,puan,otelDokumanId,otelYorumId,kullanici)

                            otelYorumlariListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }


        }


    }



}