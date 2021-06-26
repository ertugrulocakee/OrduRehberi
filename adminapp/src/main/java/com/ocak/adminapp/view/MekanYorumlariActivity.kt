package com.ocak.adminapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ocak.adminapp.R

import com.ocak.adminapp.adapter.MekanYorumlariAdapter

import com.ocak.adminapp.model.MekanYorumlari

import kotlinx.android.synthetic.main.activity_mekan_yorumlari.*

class MekanYorumlariActivity : AppCompatActivity() {

    var mekanYorumlariListesi = ArrayList<MekanYorumlari>()
    private lateinit var database : FirebaseFirestore
    private  lateinit var recyclerAdapter: MekanYorumlariAdapter
    var mekanDokumanId= ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mekan_yorumlari)


        database = FirebaseFirestore.getInstance()

        mekanDokumanId = intent.getStringExtra("mekanId").toString()


        verileriAl()


        val layoutManager = LinearLayoutManager(this)
        recyclerViewMekanYorumlari.layoutManager = layoutManager
        recyclerAdapter = MekanYorumlariAdapter(mekanYorumlariListesi)
        recyclerViewMekanYorumlari.adapter = recyclerAdapter





    }

    fun verileriAl(){
        database.collection("GeziMekanlari").document(mekanDokumanId).collection("MekanYorumlari").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->

            if(exception != null ){
                Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if(snapshot != null){

                    if(snapshot.isEmpty == false){

                        val documents = snapshot.documents

                        mekanYorumlariListesi.clear()

                        for ( document in documents){


                            val kullaniciAdi = document.get("yorumuYapanKullanici") as String
                            val kullaniciYorum = document.get("mekanYorumu") as String
                            val mekanPuani = document.get("mekanPuan") as String
                            val mekanYorumId =document.id

                            val puan ="Puanım:"+" "+mekanPuani

                            val indirilen = MekanYorumlari(kullaniciAdi,kullaniciYorum,puan,mekanDokumanId,mekanYorumId)

                            mekanYorumlariListesi.add(indirilen)


                        }

                        recyclerAdapter.notifyDataSetChanged()


                    }

                }


            }


        }

    }

}